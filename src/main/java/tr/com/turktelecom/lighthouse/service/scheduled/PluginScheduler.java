package tr.com.turktelecom.lighthouse.service.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
import tr.com.turktelecom.lighthouse.repository.UserRepository;
import tr.com.turktelecom.lighthouse.service.MailService;
import tr.com.turktelecom.lighthouse.service.PluginService;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Scheduler class for Plugins.
 */
@Component
public class PluginScheduler {

    private final Logger log = LoggerFactory.getLogger(PluginScheduler.class);

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private PluginService pluginService;

    @Inject
    private MailService mailService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Environment environment;

    /**
     * Plugin scheduler method, that will execute other plugins
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void runPlugins() {
        log.debug("Plugin scheduler started running...");
        ZonedDateTime now = ZonedDateTime.now();
        List<Plugin> plugins = pluginRepository.findDistinctByActivatedIsTrueAndNextRunDateBefore(now);
        for (Plugin plugin : plugins) {
            log.debug("Running plugin : {}", plugin.getName());
            Scan lastScan =  pluginService.startNewScan(plugin);
            if (!"dev".equals(environment.getProperty("spring.profiles.active"))) {
                plugin.setNextRunDate(now.plusHours(plugin.getSchedule()));
            }
            pluginService.addScan(plugin, lastScan);
            if (lastScan.hasAnyDefectsMoreSevereThan(Severity.HIGH)) {
                userRepository.findAll().forEach(user -> {
                    if (!user.getActivated()) {
                        return;
                    }
                    String baseUrl = environment.getProperty("server.url") + ":" + environment.getProperty("server.ui-port");
                    mailService.scanCreatedEmail(user, plugin, lastScan, baseUrl);
                });
            }
            log.debug("Finished running plugin : {}", plugin.getName());
        }
    }
}

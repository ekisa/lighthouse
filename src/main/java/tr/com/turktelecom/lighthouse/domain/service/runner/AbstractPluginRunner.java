package tr.com.turktelecom.lighthouse.domain.service.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.collector.PluginResultCollector;
import tr.com.turktelecom.lighthouse.repository.ScanRepository;
import tr.com.turktelecom.lighthouse.service.util.DateTimeUtil;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by 010235 on 22.04.2016.
 */
public abstract class AbstractPluginRunner implements PluginRunner {

    @Inject
    protected PluginResultCollector resultCollector;

    @Inject
    protected Environment environment;

    @Inject
    private ScanRepository scanRepository;

    private final Logger log = LoggerFactory.getLogger(AbstractPluginRunner.class);

    @Override
    public Scan run(Plugin plugin) throws PluginContextNotSupportedException, PluginRunFailedException {
        ZonedDateTime pluginStartTimestamp = ZonedDateTime.now();
        runInternal(plugin, pluginStartTimestamp);
        Scan previousScan = null;
        if (plugin.getId() != null) {
            previousScan = scanRepository.findTopByPluginIdOrderByCreatedDateDesc(plugin.getId());
        }
        Scan lastScan = resultCollector.collectResults(plugin);
        lastScan.setTitle(DateTimeUtil.formatTimeStamp(ZonedDateTime.now(), DateTimeUtil.PATTERN.DATE_TIME_PATTERN));
        lastScan.analyzeDifferences(previousScan);

        return lastScan;
    }


    protected abstract void runInternal(Plugin plugin, ZonedDateTime pluginStartTimestamp) throws PluginContextNotSupportedException, PluginRunFailedException;



}

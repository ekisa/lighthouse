package tr.com.turktelecom.lighthouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.turktelecom.lighthouse.domain.Authority;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.User;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.PluginRunner;
import tr.com.turktelecom.lighthouse.repository.AuthorityRepository;
import tr.com.turktelecom.lighthouse.repository.PersistentTokenRepository;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
import tr.com.turktelecom.lighthouse.repository.UserRepository;
import tr.com.turktelecom.lighthouse.repository.search.UserSearchRepository;
import tr.com.turktelecom.lighthouse.security.SecurityUtils;
import tr.com.turktelecom.lighthouse.service.util.RandomUtil;
import tr.com.turktelecom.lighthouse.web.rest.dto.ManagedUserDTO;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing plugins.
 */
@Service
@Transactional
public class PluginService {

    private final Logger log = LoggerFactory.getLogger(PluginService.class);

    @Inject
    private PluginRunner pluginRunner;

    @Inject
    private PluginRepository pluginRepository;

    //TODO : Transactional oldu mu, job çalıştırılarak kontrol edilecek
    //TODO : Exceptionlar handle edilecek
    public void runPlugin(Plugin plugin) {
        try {
            pluginRunner.run(plugin);
        } catch (PluginContextNotSupportedException e) {
            e.printStackTrace();
        } catch (PluginRunFailedException e) {
            e.printStackTrace();
        }
    }
}

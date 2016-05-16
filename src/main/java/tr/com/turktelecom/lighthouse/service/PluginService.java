package tr.com.turktelecom.lighthouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.runner.PluginRunner;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
import tr.com.turktelecom.lighthouse.service.util.DebugUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Service class for managing plugins.
 */
@Service
public class PluginService {

    private final Logger log = LoggerFactory.getLogger(PluginService.class);

    @Inject
    private PluginRunner pluginRunner;

    @PersistenceContext
    private EntityManager entityManager;

    //TODO : Transactional olma gereksinimleri incelenecek
    //TODO : Exceptionlar handle edilecek
    public Scan runPlugin(Plugin plugin) {
        try {
            return pluginRunner.run(plugin);
        } catch (PluginContextNotSupportedException e) {
            e.printStackTrace();
        } catch (PluginRunFailedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public void persist(Plugin plugin, Scan scan) {
        //DebugUtils.showTransactionStatus("PluginService.persist");
        plugin = entityManager.merge(plugin);
        plugin.addScan(scan);
        //DebugUtils.showTransactionStatus("PluginService.persist2");
    }
}
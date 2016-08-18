package tr.com.turktelecom.lighthouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginContextNotSupportedException;
import tr.com.turktelecom.lighthouse.domain.exceptions.PluginRunFailedException;
import tr.com.turktelecom.lighthouse.domain.service.runner.PluginRunner;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
import tr.com.turktelecom.lighthouse.repository.ScanRepository;
import tr.com.turktelecom.lighthouse.service.util.DebugUtils;
import tr.com.turktelecom.lighthouse.web.rest.dto.PluginHomeDTO;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //TODO : Exceptionlar handle edilecek
    public Scan startNewScan(Plugin plugin) {
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
    public Plugin addScan(Plugin plugin, Scan scan) {
        //DebugUtils.showTransactionStatus("PluginService.addScan");
        Plugin updatedPlugin = entityManager.merge(plugin);
        updatedPlugin.addScan(scan);
        return updatedPlugin;
        //DebugUtils.showTransactionStatus("PluginService.addScan");
    }

}

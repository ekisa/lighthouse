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

    @Inject private PluginRepository pluginRepository;

    @Inject private ScanRepository scanRepository;

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


    public List<PluginHomeDTO> getLastScanDefectsCountGroupedBySeverity(Pageable pageable) {

//        Page<Plugin> plugins = pluginRepository.findAll(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), new Sort("id")));
//        Map<Long, PluginHomeDTO> map = new HashMap<Long, PluginHomeDTO>();
//        for (Plugin plugin : plugins) {
//            Scan lastScan = scanRepository.findTopByPluginIdOrderByCreatedDateDesc(plugin.getId());
//            int critical=0, high=0, medium = 0, low = 0, info = 0;
//            for (Defect defect : lastScan.getDefects()) {
//                switch (defect.getSeverity()) {
//                    case CRITICAL: critical++;
//                    case HIGH: high++;
//                    case MEDIUM: medium++;
//                    case LOW: low++;
//                    case INFO: info++;
//                }
//            }
//
//            PluginHomeDTO pluginHomeDTO = new PluginHomeDTO();
//            pluginHomeDTO.setPluginId(plugin.getId());
//            pluginHomeDTO.setLastScanId(lastScan.getId());
//            pluginHomeDTO.setPluginName(plugin.getName());
//            //pluginHomeDTO.set
//
//
//        }
        return null;
    }
}

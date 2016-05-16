package tr.com.turktelecom.lighthouse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.turktelecom.lighthouse.domain.*;
import tr.com.turktelecom.lighthouse.repository.*;
import tr.com.turktelecom.lighthouse.repository.search.DefectSearchRepository;
import tr.com.turktelecom.lighthouse.repository.search.UserSearchRepository;
import tr.com.turktelecom.lighthouse.security.SecurityUtils;
import tr.com.turktelecom.lighthouse.service.util.RandomUtil;
import tr.com.turktelecom.lighthouse.web.rest.dto.DefectDTO;
import tr.com.turktelecom.lighthouse.web.rest.dto.ManagedUserDTO;
import tr.com.turktelecom.lighthouse.web.rest.mapper.DefectMapper;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class DefectService {

    @Inject
    private ScanRepository scanRepository;

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private DefectSearchRepository defectSearchRepository;

    @Inject
    private DefectMapper defectMapper;

    private final Logger log = LoggerFactory.getLogger(DefectService.class);

    public DefectDTO createDefect(DefectDTO defectDTO) {
        return Optional.ofNullable(defectMapper.defectDTOToDefect(defectDTO)).map(defect -> {
            Defect newDefect = defectRepository.save(defect);
            defectSearchRepository.save(defect);
            log.debug("Created Defect: {}", newDefect);
            return defectMapper.defectToDefectDTO(newDefect);
        }).orElse(null);

//        Defect defect = new Defect();
//        defect.setSourceIP(defectDTO.getSourceIP());
//        defect.setTitle(defectDTO.getTitle());
//        defect.setExplanation(defectDTO.getExplanation());
//        defect.setCode(defectDTO.getCode());
//        Optional.ofNullable(defectDTO.getScan()).ifPresent(scanId -> {
//            Scan scan = scanRepository.findOne(Long.valueOf(scanId));
//            defect.setScan(scan);
//            }
//        );
//        defect.setSeverity(Severity.valueOf(defectDTO.getSeverity()));
//        defect.setFalsePositive(defectDTO.getFalsePositive());
//        defect.setPort(defectDTO.getPort());
//        defect.setProtocol(defectDTO.getProtocol());
//        defect.setNeedManuelCheck(defectDTO.getNeedManuelCheck());
//        defect.setHostName(defectDTO.getHostName());
//        Optional.ofNullable(defectDTO.getPreviousDefect()).ifPresent(previousDefectId -> {
//            Defect previousDefect = defectRepository.findOne(Long.valueOf(previousDefectId));
//            defect.setPreviousDefect(previousDefect);
//        });

    }

    public DefectDTO updateDefect(DefectDTO defectDTO) {
        return Optional.ofNullable(defectMapper.defectDTOToDefect(defectDTO)).map(defect -> {
//            defect.setSourceIP(defectDTO.getSourceIP());
//            defect.setTitle(defectDTO.getTitle());
//            defect.setExplanation(defectDTO.getExplanation());
//            defect.setCode(defectDTO.getCode());
//            Optional.ofNullable(defectDTO.getScan()).ifPresent(scanId -> {
//                    Scan scan = scanRepository.findOne(Long.valueOf(scanId));
//                    defect.setScan(scan);
//                }
//            );
//            defect.setSeverity(Severity.valueOf(defectDTO.getSeverity()));
//            defect.setFalsePositive(defectDTO.getFalsePositive());
//            defect.setPort(defectDTO.getPort());
//            defect.setProtocol(defectDTO.getProtocol());
//            defect.setNeedManuelCheck(defectDTO.getNeedManuelCheck());
//            defect.setHostName(defectDTO.getHostName());
//
//            Optional.ofNullable(defectDTO.getPreviousDefect()).map(previousDefectId -> {
//                Defect previousDefect = defectRepository.findOne(Long.valueOf(previousDefectId));
//                defect.setPreviousDefect(previousDefect);
//                return defect;
//            }).orElseGet(() -> {
//                defect.setPreviousDefect(null);
//                return defect;
//            });

            Defect updatedDefect = defectRepository.save(defect);
            defectSearchRepository.save(defect);
            log.debug("Updated Defect: {}", updatedDefect);
            return defectMapper.defectToDefectDTO(updatedDefect);
        }).orElse(null);
    }
}

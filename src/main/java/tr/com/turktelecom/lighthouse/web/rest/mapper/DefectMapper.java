package tr.com.turktelecom.lighthouse.web.rest.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.repository.DefectRepository;
import tr.com.turktelecom.lighthouse.repository.ScanRepository;
import tr.com.turktelecom.lighthouse.web.rest.dto.DefectDTO;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by 010235 on 10.05.2016.
 */
@Mapper
public abstract class DefectMapper {

    @Inject private ScanRepository scanRepository;
    @Inject private DefectRepository defectRepository;

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "sourceIP", target = "sourceIP"),
        @Mapping(source = "title", target = "title"),
        @Mapping(source = "explanation", target = "explanation"),
        @Mapping(source = "code", target = "code"),
        @Mapping(source = "scan.id", target = "scan"),
        @Mapping(source = "severity", target = "severity"),
        @Mapping(source = "falsePositive", target = "falsePositive"),
        @Mapping(source = "port", target = "port"),
        @Mapping(source = "protocol", target = "protocol"),
        @Mapping(source = "needManuelCheck", target = "needManuelCheck"),
        @Mapping(source = "hostName", target = "hostName"),
        @Mapping(source = "previousDefect.id", target = "previousDefect"),
    })
    public abstract DefectDTO defectToDefectDTO(Defect defect);

    @InheritInverseConfiguration
    public abstract Defect defectDTOToDefect(DefectDTO defectDTO);



    public Scan scanIdToScan(String scanId) {
        return Optional.ofNullable(scanId).map(s -> {
            return scanRepository.findOne(Long.valueOf(s));
        }).orElse(null);
    }

    public Defect previousDefectIdToPreviousDefect(String previousDefectId) {
        return Optional.ofNullable(previousDefectId).map(d -> {
            return defectRepository.findOne(Long.valueOf(d));
        }).orElse(null);
    }
}

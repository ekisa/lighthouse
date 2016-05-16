package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tr.com.turktelecom.lighthouse.domain.Defect;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Defect entity.
 */
public interface DefectRepository extends JpaRepository<Defect,Long>, JpaSpecificationExecutor <Defect> {
    Page<Defect> findAllByScanId(Pageable pageable, Long scanId);

    Optional<Defect> findOneById(Long defectId);

}

package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.turktelecom.lighthouse.domain.Project;
import tr.com.turktelecom.lighthouse.domain.Scan;
import java.util.List;
import java.util.Optional;
/**
 * Spring Data JPA repository for the Scan entity.
 */
public interface ScanRepository extends JpaRepository<Scan,Long> {
    Page<Scan> findAllByProjectId(Pageable pageable, Long projectId);
}

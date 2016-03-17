package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.com.turktelecom.lighthouse.domain.Project;
import tr.com.turktelecom.lighthouse.domain.Scan;

/**
 * Spring Data JPA repository for the Scan entity.
 */
public interface ScanRepository extends JpaRepository<Scan,Long> {

}

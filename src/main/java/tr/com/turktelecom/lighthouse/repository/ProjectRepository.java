package tr.com.turktelecom.lighthouse.repository;

import tr.com.turktelecom.lighthouse.domain.Project;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {

}

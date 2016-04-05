package tr.com.turktelecom.lighthouse.repository;

import tr.com.turktelecom.lighthouse.domain.Plugin;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long> {

}

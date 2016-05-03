package tr.com.turktelecom.lighthouse.repository;

import tr.com.turktelecom.lighthouse.domain.Plugin;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long> {

    @EntityGraph(value = "graph.Plugin.args", type = EntityGraph.EntityGraphType.LOAD)
    List<Plugin> findAllByActivatedIsTrueAndNextRunDateBefore(ZonedDateTime dateTime);

}

package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.domain.Scan;

/**
 * Spring Data JPA repository for the Scan entity.
 */
public interface ScanRepository extends JpaRepository<Scan,Long> {
    Page<Scan> findAllByPluginId(Pageable pageable, Long pluginId);

    @EntityGraph(value = "graph.Scan.defects", type = EntityGraph.EntityGraphType.LOAD)
    Scan findTopByPluginIdOrderByCreatedDateDesc(Long pluginId);
}

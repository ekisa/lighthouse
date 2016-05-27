package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import tr.com.turktelecom.lighthouse.domain.Plugin;

import org.springframework.data.jpa.repository.*;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long> {

    @EntityGraph(value = "graph.Plugin.args", type = EntityGraph.EntityGraphType.LOAD)
    List<Plugin> findAllByActivatedIsTrueAndNextRunDateBefore(ZonedDateTime dateTime);

    @Query(value = "select p.id as pluginId, p.name as pluginName, s.id as lastScanId, d.severity as severity, count(1) as defectCount" +
        " from Plugin p " +
        " left outer join p.scans as s " +
        " left outer join s.defects as d" +
        " where p.id in :pluginIds" +
        " and (p.id, s.createdDate) in " +
        " (select s.plugin.id, max(s.createdDate) from Scan as s group by s.plugin.id) " +
        " group by p.id, p.name, d.severity " +
        " order by CASE WHEN :sort = 'pluginId ASC' THEN p.id END ASC" +
        " ,CASE WHEN :sort = 'pluginId DESC' THEN p.id END DESC" +
        " ,CASE WHEN :sort = 'pluginName ASC' THEN p.name END ASC " +
        " ,CASE WHEN :sort = 'pluginName DESC' THEN p.name END DESC " +
        " ,CASE WHEN :sort = 'severity ASC' THEN d.severity END ASC " +
        " ,CASE WHEN :sort = 'severity DESC' THEN d.severity END DESC ")
    List<Object[]> findPluginHomeStats(@Param(value = "sort") String sort, @Param(value = "pluginIds")List<Long> pluginsIds);
}

package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import tr.com.turktelecom.lighthouse.domain.Plugin;

import org.springframework.data.jpa.repository.*;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.domain.Severity;

import javax.persistence.NamedNativeQuery;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Plugin entity.
 */
public interface PluginRepository extends JpaRepository<Plugin,Long> {

    @EntityGraph(value = "graph.Plugin.args", type = EntityGraph.EntityGraphType.LOAD)
    List<Plugin> findDistinctByActivatedIsTrueAndNextRunDateBefore(ZonedDateTime dateTime);

    @Query(value =
            "select p.id as pluginId, p.name as pluginName, s.id as lastScanId, d.severity as severity, count(1) as defectCount" +
            " from Plugin p " +
            " left outer join p.scans as s " +
            " left outer join s.defects as d" +
            " where p.id in :pluginIds" +
            " and (p.id, s.createdDate) in " +
            " (select s.plugin.id, max(s.createdDate) from Scan as s group by s.plugin.id) " +
            " group by p.id, p.name, d.severity" +
        " order by CASE WHEN :sort = 'pluginId ASC' THEN p.id END ASC" +
        " ,CASE WHEN :sort = 'pluginId DESC' THEN p.id END DESC" +
        " ,CASE WHEN :sort = 'pluginName ASC' THEN p.name END ASC " +
        " ,CASE WHEN :sort = 'pluginName DESC' THEN p.name END DESC " +
        " ,CASE WHEN :sort = 'severity ASC' THEN d.severity END DESC, count(1) ASC" +
        " ,CASE WHEN :sort = 'severity DESC' THEN d.severity END DESC , count(1) DESC")
    List<Object[]> findPluginHomeStats(@Param(value = "sort") String sort, @Param(value = "pluginIds")List<Long> pluginsIds);

//    @Query(nativeQuery = true, value= "select x.* from(" +
//        "select p.id as pluginId, p.name as pluginName, s.id as lastScanId, d.severity as severity, count(1) as defectCount, " +
//        " case when d.severity = 'CRITICAL' then 4" +
//        " when d.severity = 'HIGH' then 3" +
//        " when d.severity = 'MEDIUM' then 2" +
//        " when d.severity = 'LOW' then 1" +
//        " when d.severity = 'INFO' then 0 end as severityOrder" +
//        " from Plugin p " +
//        " left outer join Scan as s on s.plugin_id = p.id" +
//        " left outer join Defect as d on d.scan_id = s.id" +
//        " where p.id in :pluginIds" +
//        " and (p.id, s.created_date) in " +
//        " (select s.plugin_id, max(s.created_date) from Scan as s group by s.plugin_id) " +
//        " group by p.id, p.name, d.severity" +
//        ") as x " +
//        " order by CASE WHEN :sort = 'pluginId ASC' THEN pluginId END ASC" +
//        " ,CASE WHEN :sort = 'pluginId DESC' THEN pluginId END DESC" +
//        " ,CASE WHEN :sort = 'pluginName ASC' THEN pluginName END ASC " +
//        " ,CASE WHEN :sort = 'pluginName DESC' THEN pluginName END DESC " +
//        " ,CASE WHEN :sort = 'severity ASC' THEN severityOrder END DESC,defectCount ASC " +
//        " ,CASE WHEN :sort = 'severity DESC' THEN severityOrder END DESC,defectCount DESC ")
//    List<Object[]> findPluginHomeStats(@Param(value = "sort") String sort, @Param(value = "pluginIds")List<Long> pluginsIds);
}

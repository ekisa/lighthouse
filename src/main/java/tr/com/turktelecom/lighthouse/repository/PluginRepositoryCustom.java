package tr.com.turktelecom.lighthouse.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import tr.com.turktelecom.lighthouse.web.rest.dto.PluginHomeDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by 010235 on 26.05.2016.
 */
@Repository
public class PluginRepositoryCustom {

    @PersistenceContext EntityManager entityManager;
//
//    public List<PluginHomeDTO> defectsCountGroupedBySeverity(List<Long> pluginIds, Pageable pageable){
//        Query query = entityManager.createNativeQuery(
//            "select * from (select p.id as pluginId, p.name as pluginName, s.id as scanId, d.severity as severity, count(1) as defectCount, " +
//                " case when severity='INFO' then 0 " +
//                    " when severity='LOW' then 1 " +
//                    " when severity='MEDIUM' then 2 " +
//                    " when severity='HIGH' then 3 " +
//                    " when severity='CRITICAL' then 4 end as severityOrder " +
//                " from Plugin p " +
//                " left outer join (select p.id as pluginId, max(s.id) as maxScanId from Plugin p  " +
//                    " left outer join Scan s on s.plugin_id = p.id " +
//                    " group by p.id) m on p.id = m.pluginId " +
//                " left outer join Scan s on s.id = m.maxScanId " +
//                " left outer join Defect d on d.scan_id = m.maxScanId " +
//                " where p.id in (:pluginIds) " +
//                " group by p.id, p.name, s.id, d.severity, severityOrder " +
//                " order by " +
//                " CASE WHEN :sort = 'pluginId ASC' THEN pluginId END ASC" +
//                " ,CASE WHEN :sort = 'pluginId DESC' THEN pluginId END DESC" +
//                " ,CASE WHEN :sort = 'pluginName ASC' THEN pluginName END ASC " +
//                " ,CASE WHEN :sort = 'pluginName DESC' THEN pluginName END DESC " +
//                " ,CASE WHEN :sort = 'severityOrder ASC' THEN severityOrder END ASC " +
//                " ,CASE WHEN :sort = 'severityOrder DESC' THEN severityOrder END DESC, pluginId desc ) as x " +
//                " limit " + pageable.getPageSize() + " offset " + pageable.getOffset()
//        );
//        List<PluginHomeDTO> list = new ArrayList<PluginHomeDTO>();
//        query.setParameter("pluginIds", pluginIds);
//        String sort = pageable.getSort().toString().replace(":", "");
//        int i = sort.indexOf(",");
//        if (i > 0) {
//            sort = sort.substring(0, i);
//        }
//        query.setParameter("sort", sort);
//        //query.setMaxResults(pageable.getPageSize());
//        //query.setFirstResult(pageable.getOffset());
//
//        List<Object[]> results = (List<Object[]>) query.getResultList();
//
//        Map<Long, PluginHomeDTO> pluginHomeDTOMap = new HashMap<Long, PluginHomeDTO>();
//        List<Long> orderedKeys = new ArrayList<Long>();
//        for (Object[] result : results) {
//            Long plugin = result[0] != null ? Long.valueOf(result[0].toString()) : null;
//            String pluginName = result[1] != null ? result[1].toString() : "";
//            Long lastScanId = result[2] != null ? Long.valueOf(result[2].toString()) : null;
//            String severity = result[3] != null ? result[3].toString() : "";
//            Long defectCount = result[4] != null ? Long.valueOf(result[4].toString()) : null;
//            Integer severityOrder = result[5] != null ? Integer.valueOf(result[5].toString()) : null;
//            PluginHomeDTO pluginHomeDTO = pluginHomeDTOMap.get(plugin);
//            if (pluginHomeDTO == null) {
//                HashMap<String, Long> defectsCountMap = new HashMap<String, Long>();
//                defectsCountMap.put(severity, defectCount);
//                orderedKeys.add(plugin);
//                pluginHomeDTO = new PluginHomeDTO(plugin, pluginName, lastScanId, defectsCountMap, severityOrder);
//                pluginHomeDTOMap.put(plugin, pluginHomeDTO);
//            }else{
//                pluginHomeDTO.getDefectsCountBySeverity().put(severity, defectCount);
//            }
//        }
//
//        for (Long key : orderedKeys) {
//            PluginHomeDTO pluginHomeDTO = pluginHomeDTOMap.get(key);
//            list.add(pluginHomeDTO);
//        }
//
//        return list;
//    }


    public List<PluginHomeDTO> findLastScanWithDefects(Pageable pageable){
        Query query = entityManager.createQuery(
            "select p.id, p.name, d.severity, count(1) as defectCount" +
                " from Plugin p " +
                " left outer join p.scans as s " +
                " left outer join s.defects as d" +
                " where (p.id, s.createdDate) in " +
                " (select s.plugin.id, max(s.createdDate) from Scan as s group by s.plugin.id) " +
                " or s.id is null " +
                " group by p.id, p.name, d.severity " +
                " order by :sort"
        );
        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult(pageable.getOffset());
        String sort = pageable.getSort().toString().replace(":", "");
        int i = sort.indexOf(",");
        if (i > 0) {
            sort = sort.substring(0, i);
        }
        query.setParameter("sort", sort);
        List<Object[]> resultList = query.getResultList();

        for (Object[] result : resultList) {
            System.out.println(result);
        }

        return null;
    }
}

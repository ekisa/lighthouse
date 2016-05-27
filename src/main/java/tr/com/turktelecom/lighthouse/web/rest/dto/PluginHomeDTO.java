package tr.com.turktelecom.lighthouse.web.rest.dto;

import tr.com.turktelecom.lighthouse.domain.Severity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 010235 on 26.05.2016.
 */
public class PluginHomeDTO {
    private Long pluginId;
    private String pluginName;
    private Long lastScanId;
    private Map<String, Long> defectsCountBySeverity = new HashMap<String, Long>();
    private Severity severity;

    public PluginHomeDTO() {
    }

    public PluginHomeDTO(Long pluginId, String pluginName, Long lastScanId, Map<String, Long> defectsCountBySeverity, Severity severity) {
        this.pluginId = pluginId;
        this.pluginName = pluginName;
        this.lastScanId = lastScanId;
        this.defectsCountBySeverity = defectsCountBySeverity;
        this.severity = severity;
    }

    public Long getPluginId() {
        return pluginId;
    }

    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public Long getLastScanId() {
        return lastScanId;
    }

    public void setLastScanId(Long lastScanId) {
        this.lastScanId = lastScanId;
    }

    public Map<String, Long> getDefectsCountBySeverity() {
        return defectsCountBySeverity;
    }

    public void setDefectsCountBySeverity(Map<String, Long> defectsCountBySeverity) {
        this.defectsCountBySeverity = defectsCountBySeverity;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public static List<PluginHomeDTO> createFrom(List<Object[]> results) {
        List<PluginHomeDTO> list = new ArrayList<PluginHomeDTO>();
        Map<Long, PluginHomeDTO> pluginHomeDTOMap = new HashMap<Long, PluginHomeDTO>();
        List<Long> orderedKeys = new ArrayList<Long>();
        for (Object[] result : results) {
            Long plugin = result[0] != null ? Long.valueOf(result[0].toString()) : null;
            String pluginName = result[1] != null ? result[1].toString() : null;
            Long lastScanId = result[2] != null ? Long.valueOf(result[2].toString()) : null;
            Severity severity = result[3] != null ? (Severity) result[3] : null;
            Long defectCount = result[4] != null ? Long.valueOf(result[4].toString()) : null;
            PluginHomeDTO pluginHomeDTO = pluginHomeDTOMap.get(plugin);
            if (pluginHomeDTO == null) {
                HashMap<String, Long> defectsCountMap = new HashMap<String, Long>();
                defectsCountMap.put(severity != null ? severity.getTitle() : "", defectCount);
                orderedKeys.add(plugin);
                pluginHomeDTO = new PluginHomeDTO(plugin, pluginName, lastScanId, defectsCountMap, severity);
                pluginHomeDTOMap.put(plugin, pluginHomeDTO);
            }else{
                pluginHomeDTO.getDefectsCountBySeverity().put(severity != null ? severity.getTitle() : "", defectCount);
            }
        }

        for (Long key : orderedKeys) {
            PluginHomeDTO pluginHomeDTO = pluginHomeDTOMap.get(key);
            list.add(pluginHomeDTO);
        }

        return list;
    }
}

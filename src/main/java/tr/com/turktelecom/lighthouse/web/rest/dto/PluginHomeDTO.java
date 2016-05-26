package tr.com.turktelecom.lighthouse.web.rest.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 010235 on 26.05.2016.
 */
public class PluginHomeDTO {
    private Long pluginId;
    private String pluginName;
    private Long lastScanId;
    private Map<String, Long> defectsCountBySeverity = new HashMap<String, Long>();
    private Integer severityOrder;

    public PluginHomeDTO() {
    }

    public PluginHomeDTO(Long pluginId, String pluginName, Long lastScanId, Map<String, Long> defectsCountBySeverity, Integer severityOrder) {
        this.pluginId = pluginId;
        this.pluginName = pluginName;
        this.lastScanId = lastScanId;
        this.defectsCountBySeverity = defectsCountBySeverity;
        this.severityOrder = severityOrder;
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

    public Integer getSeverityOrder() {
        return severityOrder;
    }

    public void setSeverityOrder(Integer severityOrder) {
        this.severityOrder = severityOrder;
    }
}

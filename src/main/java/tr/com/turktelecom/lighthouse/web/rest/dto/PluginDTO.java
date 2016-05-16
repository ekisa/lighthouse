package tr.com.turktelecom.lighthouse.web.rest.dto;

import tr.com.turktelecom.lighthouse.domain.Plugin;

import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 010235 on 10.05.2016.
 */
public class PluginDTO extends EntityDTO{

    private String name;
    private String explanation;
    @Pattern(regexp = "[a-zA-Z0-9-_.]*")
    @Size(max = 20)
    private String folderName;
    private String schedule;
    private Boolean activated;
    private ZonedDateTime nextRunDate;
    private Set<String> scans = new HashSet<String>();
    private String pluginContext;
    private String outputFormat;
    private Map<String,String> args = new HashMap<String, String>();;


    public PluginDTO() {
    }

    public PluginDTO(Plugin plugin) {
        this(plugin.getId(), plugin.getName(), plugin.getExplanation(), plugin.getFolderName(),
            plugin.getSchedule(), plugin.getActivated(), plugin.getNextRunDate(),
            plugin.getScans().stream().map(scan -> scan.getId().toString()).collect(Collectors.toSet())
            , plugin.getExplanation(), plugin.getOutputFormat(),
            plugin.getArgs());
    }

    public PluginDTO(Long id, String name, String explanation, String folderName,
                     String schedule, Boolean activated, ZonedDateTime nextRunDate,
                     Set<String> scans, String pluginContext, String outputFormat,
                     Map<String, String> args) {
        this.id = id;
        this.name = name;
        this.explanation = explanation;
        this.folderName = folderName;
        this.schedule = schedule;
        this.activated = activated;
        this.nextRunDate = nextRunDate;
        this.scans = scans;
        this.pluginContext = pluginContext;
        this.outputFormat = outputFormat;
        this.args.clear();
        this.args.putAll(args);
    }

    @Override
    public String toString() {
        return "PluginDTO{" +
            "name='" + name + '\'' +
            ", explanation='" + explanation + '\'' +
            ", folderName='" + folderName + '\'' +
            ", schedule='" + schedule + '\'' +
            ", activated=" + activated +
            ", nextRunDate=" + nextRunDate +
            ", scans=" + scans +
            ", pluginContext='" + pluginContext + '\'' +
            ", outputFormat='" + outputFormat + '\'' +
            ", args=" + args +
            '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public ZonedDateTime getNextRunDate() {
        return nextRunDate;
    }

    public void setNextRunDate(ZonedDateTime nextRunDate) {
        this.nextRunDate = nextRunDate;
    }

    public Set<String> getScans() {
        return scans;
    }

    public void setScans(Set<String> scans) {
        this.scans = scans;
    }

    public String getPluginContext() {
        return pluginContext;
    }

    public void setPluginContext(String pluginContext) {
        this.pluginContext = pluginContext;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Map<String, String> getArgs() {
        return args;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }
}

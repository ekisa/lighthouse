package tr.com.turktelecom.lighthouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * A Plugin.
 */
@Entity
@Table(name = "plugin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "plugin")
@NamedEntityGraphs(value = {
    @NamedEntityGraph(name = "graph.Plugin.args", attributeNodes = @NamedAttributeNode("args"))
})
public class Plugin extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "folder_Name")
    @Pattern(regexp = "[a-zA-Z0-9-_.]*")
    @Length(max = 20)
    private String folderName;

    @Column(name = "schedule")
    private Long schedule;

    @Column(name = "activated")
    private Boolean activated;

    @Column(name = "next_run_date")
    private ZonedDateTime nextRunDate = ZonedDateTime.now();

    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL )
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 20)
    //@OrderColumn(name = "created_date")
    private List<Scan> scans = new ArrayList<Scan>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plugin_context")
    @Fetch(FetchMode.JOIN)
    private PluginContext pluginContext;

    @Column(name = "output_format")
    private String outputFormat;

    @OneToMany(mappedBy = "plugin", cascade = CascadeType.ALL )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private List<PluginArgument> args = new ArrayList<PluginArgument>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getSchedule() {
        return schedule;
    }

    public void setSchedule(Long schedule) {
        this.schedule = schedule;
    }

    public List<Scan> getScans() {
        return scans;
    }

    public void setScans(List<Scan> scans) {
        this.scans = scans;
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

    public PluginContext getPluginContext() {
        return pluginContext;
    }

    public void setPluginContext(PluginContext pluginContext) {
        this.pluginContext = pluginContext;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<PluginArgument> getArgs() {
        return args;
    }

    public void setArgs(List<PluginArgument> args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Plugin plugin = (Plugin) o;
        if(plugin.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, plugin.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Plugin{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", explanation='" + explanation + '\'' +
            ", folderName='" + folderName + '\'' +
            ", schedule='" + schedule + '\'' +
            ", activated=" + activated +
            ", nextRunDate=" + nextRunDate +
            ", outputFormat='" + outputFormat + '\'' +
            '}';
    }

    public void addScan(Scan scan) {
        scan.setPlugin(this);
        this.getScans().add(scan);
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}

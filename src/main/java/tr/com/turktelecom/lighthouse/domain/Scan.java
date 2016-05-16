package tr.com.turktelecom.lighthouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.*;

/**
 * A Scan.
 */
@Entity
@Table(name = "SCAN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "scan")
@NamedEntityGraph(name = "graph.Scan.defects", attributeNodes = @NamedAttributeNode("defects"))
public class Scan extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plugin_id")
    private Plugin plugin;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.ALL)
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<Defect> defects = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public Set<Defect> getDefects() {
        return defects;
    }

    public void setDefects(Set<Defect> defects) {
        this.defects = defects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scan scan = (Scan) o;
        if(scan.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, scan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Scan{" +
            "id=" + id +
            ", title='" + title + "'" +
            //", createdDate='" + JSR310DateConverters.ZonedDateTimeToDateConverter.INSTANCE.convert(getCreatedDate()) + "'" +
            '}';
    }

    public void addDefect(Defect defect) {
        defect.setScan(this);
        this.getDefects().add(defect);
    }

    public void analyzeDifferences(Scan previousScan) {
        if (previousScan == null) {
            return;
        }

        Map<String, Defect> latestDefects = new HashMap<String, Defect>();
        for (Defect latestDefect : this.getDefects()) {
            latestDefects.put(new StringBuffer().append(latestDefect.getSourceIP()).append("-").append(latestDefect.getTitle()).toString(), latestDefect);
        }

        for (Defect previousDefect : previousScan.getDefects()) {
            String defectKey = new StringBuffer().append(previousDefect.getSourceIP()).append("-").append(previousDefect.getTitle()).toString();
            if (latestDefects.containsKey(defectKey)) {
                latestDefects.get(defectKey).setPreviousDefect(previousDefect);
            }
        }
    }
}

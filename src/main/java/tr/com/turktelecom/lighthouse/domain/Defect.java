package tr.com.turktelecom.lighthouse.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Defect.
 */
@Entity
@Table(name = "defect")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "defect")
public class Defect extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "scan_id")
    private Scan scan;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Column(name="is_false_positive")
    private Boolean isFalsePositive = Boolean.FALSE;

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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public Boolean getFalsePositive() {
        return isFalsePositive;
    }

    public void setFalsePositive(Boolean falsePositive) {
        isFalsePositive = falsePositive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Defect defect = (Defect) o;
        if(defect.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, defect.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Defect{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", explanation='" + explanation + "'" +
            ", code='" + code + "'" +
            ", severity='" + severity + "'" +
            ", isFalsePositive='" + isFalsePositive + "'" +
            '}';
    }
}

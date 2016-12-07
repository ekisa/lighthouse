package tr.com.turktelecom.lighthouse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Length;
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
//@Document(indexName = "defect")
public class Defect extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Long id;

    @Column(name = "source_ip")
    private String sourceIP;

    @Column(name = "title")
    private String title;

    @Column(name = "explanation")
    @Length(max = 2000)
    private String explanation;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scan_id")
    private Scan scan;

    @Enumerated(EnumType.ORDINAL)
    private Severity severity;

    @Column(name="false_positive")
    private Boolean falsePositive = Boolean.FALSE;

    @Column(name = "port")
    private String port;

    @Column(name = "protocol")
    private String protocol;

    @Column(name="need_manuel_check")
    private Boolean needManuelCheck;

    @Column(name = "host_name")
    private String hostName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previous_defect")
    private Defect previousDefect;

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
        return falsePositive;
    }

    public void setFalsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
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
            ", sourceIP='" + sourceIP + '\'' +
            ", hostName='" + hostName + '\'' +
            ", port='" + port + '\'' +
            ", protocol='" + protocol + '\'' +
            ", code='" + code + '\'' +
            ", severity=" + severity +
            ", falsePositive=" + falsePositive +
            ", needManuelCheck=" + needManuelCheck +
            '}';
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setNeedManuelCheck(Boolean needManuelCheck) {
        this.needManuelCheck = needManuelCheck;
    }

    public Boolean getNeedManuelCheck() {
        return needManuelCheck;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setPreviousDefect(Defect previousDefect) {
        this.previousDefect = previousDefect;
    }

    public Defect getPreviousDefect() {
        return previousDefect;
    }
}

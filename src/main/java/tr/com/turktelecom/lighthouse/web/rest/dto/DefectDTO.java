package tr.com.turktelecom.lighthouse.web.rest.dto;

import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Scan;

import java.util.Optional;

/**
 * Created by 010235 on 10.05.2016.
 */
public class DefectDTO extends EntityDTO {

    private String sourceIP;
    private String title;
    private String explanation;
    private String code;
    private String scan;
    private String severity;
    private Boolean falsePositive = Boolean.FALSE;
    private String port;
    private String protocol;
    private Boolean needManuelCheck;
    private String hostName;
    private String previousDefect;

    public DefectDTO() {
    }

    public DefectDTO(Defect defect) {
        this(defect.getId(),  defect.getSourceIP(), defect.getTitle(), defect.getExplanation(),
            defect.getCode(), Optional.ofNullable(defect.getScan()).map(Scan::getId).get().toString(), defect.getSeverity().name(),
            defect.getFalsePositive(), defect.getPort(), defect.getProtocol(),
            defect.getNeedManuelCheck(), defect.getHostName(), Optional.ofNullable(defect.getPreviousDefect()).map(Defect::getId).get().toString());
    }

    public DefectDTO(Long id, String sourceIP, String title, String explanation,
                     String code, String scan, String severity,
                     Boolean falsePositive, String port, String protocol,
                     Boolean needManuelCheck, String hostName, String previousDefect) {
        this.id = id;
        this.sourceIP = sourceIP;
        this.title = title;
        this.explanation = explanation;
        this.code = code;
        this.scan = scan;
        this.severity = severity;
        this.falsePositive = falsePositive;
        this.port = port;
        this.protocol = protocol;
        this.needManuelCheck = needManuelCheck;
        this.hostName = hostName;
        this.previousDefect = previousDefect;
    }

    @Override
    public String toString() {
        return "DefectDTO{" +
            "sourceIP='" + sourceIP + '\'' +
            ", title='" + title + '\'' +
            //", explanation='" + explanation + '\'' +
            ", code='" + code + '\'' +
            ", scan='" + scan + '\'' +
            ", severity='" + severity + '\'' +
            ", falsePositive=" + falsePositive +
            ", port='" + port + '\'' +
            ", protocol='" + protocol + '\'' +
            ", needManuelCheck=" + needManuelCheck +
            ", hostName='" + hostName + '\'' +
            ", previousDefect='" + previousDefect + '\'' +
            '}';
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
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

    public String getScan() {
        return scan;
    }

    public void setScan(String scan) {
        this.scan = scan;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Boolean getFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getNeedManuelCheck() {
        return needManuelCheck;
    }

    public void setNeedManuelCheck(Boolean needManuelCheck) {
        this.needManuelCheck = needManuelCheck;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPreviousDefect() {
        return previousDefect;
    }

    public void setPreviousDefect(String previousDefect) {
        this.previousDefect = previousDefect;
    }
}

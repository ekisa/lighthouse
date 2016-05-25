package tr.com.turktelecom.lighthouse.web.rest.dto.search;

import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotNull;

/**
 * Created by 010235 on 13.05.2016.
 */
public class DefectSearchDTO extends SearchDTO{
    @NotNull
    private Long scanId;

    public Long getScanId() {
        return scanId;
    }

    public void setScanId(Long scanId) {
        this.scanId = scanId;
    }
}

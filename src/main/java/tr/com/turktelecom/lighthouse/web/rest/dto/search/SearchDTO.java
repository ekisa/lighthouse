package tr.com.turktelecom.lighthouse.web.rest.dto.search;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.Sort;
import tr.com.turktelecom.lighthouse.domain.util.CustomSearchDeserializer;
import tr.com.turktelecom.lighthouse.domain.util.CustomSortDeserializer;

import javax.validation.constraints.NotNull;

/**
 * Created by 010235 on 13.05.2016.
 */
//@JsonDeserialize(using = SearchDTODeserializer.class)
public class SearchDTO{

    @NotNull
    private int page;
    @NotNull
    private int size;
    @JsonDeserialize(using=CustomSortDeserializer.class)
    private Sort sort;

    @JsonDeserialize(using=CustomSearchDeserializer.class)
    private Search search;

    public SearchDTO() {
    }

    public SearchDTO(int page, int size, Sort sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}



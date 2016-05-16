package tr.com.turktelecom.lighthouse.service.search;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 010235 on 13.05.2016.
 */
public class SearchSpecificationsBuilder<T> {

    private final List<SearchCriteria> params;

    public SearchSpecificationsBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public SearchSpecificationsBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = new ArrayList<Specification<T>>();
        for (SearchCriteria param : params) {
            specs.add(new SearchSpecification(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}

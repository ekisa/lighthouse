package tr.com.turktelecom.lighthouse.web.rest.dto.search;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 010235 on 17.05.2016.
 */
public class PredicateObject {

    private Map<String, String> predicates = new HashMap<String, String>();

    public PredicateObject() {
    }

    public PredicateObject(Map<String, String> predicates) {
        this.predicates = predicates;
    }

    @JsonAnyGetter
    public Map<String, String> getPredicates() {
        return predicates;
    }

    @JsonAnySetter
    public void setPredicates(Map<String, String> predicates) {
        this.predicates = predicates;
    }
}

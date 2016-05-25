package tr.com.turktelecom.lighthouse.web.rest.dto.search;

/**
 * Created by 010235 on 17.05.2016.
 */
public class Search {
    private PredicateObject predicateObject;

    public Search() {
    }

    public Search(PredicateObject predicateObject) {
        this.predicateObject = predicateObject;
    }

    public PredicateObject getPredicateObject() {
        return predicateObject;
    }

    public void setPredicateObject(PredicateObject predicateObject) {
        this.predicateObject = predicateObject;
    }
}

package tr.com.turktelecom.lighthouse.service.util;

import tr.com.turktelecom.lighthouse.domain.Defect;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 010235 on 17.05.2016.
 */
public class PersistenceUtil {

    @SuppressWarnings("unchecked")
    static public<T, R> Path<R> getPath(Class<R> resultType, Path<T> root, String path) {
        String[] pathElements = path.split("\\.");
        Path<?> retVal = root;
        for (String pathEl : pathElements) {
            retVal = (Path<R>) retVal.get(pathEl);
        }
        return (Path<R>) retVal;
    }


    static public List<Predicate> toPredicates(Map<String, String> map, CriteriaBuilder criteriaBuilder, Root<Defect> root) {
        return toPredicates(map, criteriaBuilder, root, new HashMap<String, Class>());
    }
    static public List<Predicate> toPredicates(Map<String, String> map, CriteriaBuilder criteriaBuilder, Root<Defect> root, Map<String, Class> enumParameters) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (final Map.Entry<String, String> e : map.entrySet()) {

            final String key = e.getKey();
            final String value = e.getValue();

            if ((key != null) && (value != null)) {
                if (value.contains("%")) {
                    predicates.add(criteriaBuilder.like(root.<String> get(key), value));
                } else {
                    //Bu bir enumeration ise parametrede enum değeri üretmemiz gerekiyor
                    if (enumParameters.get(key) != null) {
                        predicates.add(criteriaBuilder.equal(root.get(key), Enum.valueOf(enumParameters.get(key), value)));
                    }
                    else{
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }
                }
            }
        }
        return predicates;
    }
}

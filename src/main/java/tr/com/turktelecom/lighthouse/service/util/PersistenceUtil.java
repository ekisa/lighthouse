package tr.com.turktelecom.lighthouse.service.util;

import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Defect;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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


    static public List<Predicate> toPredicates(Map<String, String> map, CriteriaBuilder criteriaBuilder, Root<?> root, Class classz) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (final Map.Entry<String, String> entry : map.entrySet()) {

            final String key = entry.getKey();
            final String value = entry.getValue();
            try {
                if ((key != null) && (value != null)) {
                    if (value.contains("%")) {
                        predicates.add(criteriaBuilder.like(root.<String>get(key), value));
                    } else {
                        //Bu bir enumeration ise parametrede enum değeri üretmemiz gerekiyor
                        if (classz != null && classz.getDeclaredField(key).getType().isEnum()) {
                            Class<Enum> fieldType = (Class<Enum>) classz.getDeclaredField(key).getType();
                            Enum anEnum = Enum.valueOf(fieldType, value);
                            predicates.add(criteriaBuilder.equal(root.get(key), anEnum));
                        }else if(classz != null && classz.getDeclaredField(key).getType().equals(Boolean.class)){
                            Boolean aBoolean = Boolean.valueOf(value);
                            predicates.add(criteriaBuilder.equal(root.get(key), aBoolean));
                        }else if (classz != null && classz.getDeclaredField(key).getType().getPackage().getName().startsWith("tr.com.turktelecom.lighthouse.")) {
                            if ("TRUE".equalsIgnoreCase(value)) {
                                predicates.add(criteriaBuilder.isNull(root.get(key)));
                            }
                            else if("FALSE".equalsIgnoreCase(value)) {
                                predicates.add(criteriaBuilder.isNotNull(root.get(key)));
                            }
                        }
                        else {
                            predicates.add(criteriaBuilder.equal(root.get(key), value));
                        }
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return predicates;
    }
}

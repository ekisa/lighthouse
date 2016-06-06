package tr.com.turktelecom.lighthouse.service.util;

import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by 010235 on 17.05.2016.
 */
public class PersistenceUtil {

    @SuppressWarnings("unchecked")
    static public<T, R> Path<R> getPath(Path<T> root, String path) {
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

            String key = entry.getKey();
            final String value = entry.getValue();
            try {

                if ((key != null) && (value != null))
                {
                    Class propertyType = typeOf(key, classz);

                    if (value.contains("%")) {
                        predicates.add(criteriaBuilder.like(getPath(root, key), value));
                    }
                    //Bu bir enumeration ise parametrede enum değeri üretmemiz gerekiyor
                    else if (propertyType != null && propertyType.isEnum()) {
                        Enum anEnum = Enum.valueOf(propertyType, value);
                        predicates.add(criteriaBuilder.equal(getPath(root, key), anEnum));
                    }
                    else if(propertyType != null && propertyType.equals(Boolean.class) ){
                        Boolean aBoolean = Boolean.valueOf(value);
                        predicates.add(criteriaBuilder.equal(getPath(root, key), aBoolean));
                    }
                    else if (propertyType != null && propertyType.getPackage().getName().startsWith("tr.com.turktelecom.lighthouse.")) {
                        if ("TRUE".equalsIgnoreCase(value)) {
                            predicates.add(criteriaBuilder.isNull(getPath(root, key)));
                        }
                        else if("FALSE".equalsIgnoreCase(value)) {
                            predicates.add(criteriaBuilder.isNotNull(getPath(root, key)));
                        }
                    }else{
                        predicates.add(criteriaBuilder.equal(getPath(root, key), value));
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return predicates;
    }

    static private Class typeOf(String key, Class classz) throws NoSuchFieldException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        if (classz == null) {
            return null;
        }
        if (key.contains(".")) {
            List<String> properties = Arrays.asList(key.split("\\."));
            Iterator<String> i = properties.iterator();
            Object o = classz.newInstance();
            while (i.hasNext()) {
                String p = i.next();
                if (i.hasNext()) {
                    Method getterMethod = findGetterMethod(p, o.getClass());
                    o = getterMethod.getReturnType().newInstance();
                }
                else{
                    return o.getClass().getDeclaredField(p).getType();
                }
            }
        }
        return classz.getDeclaredField(key).getType();

    }

    private static Method findGetterMethod(String k, Class classz) throws NoSuchMethodException {
        char firstChar = k.charAt(0);
        String rest = k.substring(1, k.length());
        String methodName = new StringBuilder().append("get").append(Character.toUpperCase(firstChar)).append(rest).toString();
        return classz.getDeclaredMethod(methodName);
    }
}

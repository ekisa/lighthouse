package tr.com.turktelecom.lighthouse.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tr.com.turktelecom.lighthouse.service.util.PersistenceUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 010235 on 08.06.2016.
 */
@Service
public class MyFormQueryService {
    @PersistenceContext
    private EntityManager entityManager;

    public <T> Long createCountQuery(Class<T> queryClass, Map<String, Object> requiredParamsMap, Map<String, String> filterParamsMap) {
        CriteriaBuilder criteriaBuilderToCount = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQueryToCount = criteriaBuilderToCount.createQuery(Long.class);
        Root rootToCount = criteriaQueryToCount.from(queryClass);
        List<Predicate> predicateList = PersistenceUtil.toPredicates(filterParamsMap, criteriaBuilderToCount, rootToCount, queryClass);
        Iterator<Map.Entry<String, Object>> requiredParamMapIterator = requiredParamsMap.entrySet().iterator();
        while (requiredParamMapIterator.hasNext()) {
            Map.Entry<String, Object> entry = requiredParamMapIterator.next();
            Predicate requiredParamPredicate = criteriaBuilderToCount.equal(PersistenceUtil.getPath(rootToCount, entry.getKey()), entry.getValue());
            predicateList.add(requiredParamPredicate);
        }
        Predicate predicatesToCount = criteriaBuilderToCount.and(predicateList.toArray(new Predicate[predicateList.size()]));
        criteriaQueryToCount.select(criteriaBuilderToCount.count(rootToCount));
        criteriaQueryToCount.where(predicatesToCount);
        return entityManager.createQuery(criteriaQueryToCount).getSingleResult();
    }

    public <T> List<T> createSearchQuery(Class<T> queryClass, Map<String, Object> requiredParamsMap, Map<String, String> filterParamsMap, Integer page, Integer size, Sort sort) {
        List<Predicate> predicateList;
        Predicate requiredParamPredicate;
        CriteriaBuilder criteriaBuilderToSearch = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQueryToSearch = criteriaBuilderToSearch.createQuery(queryClass);
        Root rootToSearch = criteriaQueryToSearch.from(queryClass);
        List<Order> orderList = new ArrayList<Order>();
        sort.spliterator().trySplit().forEachRemaining(order -> {
            if (order.getDirection().equals(Sort.Direction.ASC)) {
                Order orderItem = criteriaBuilderToSearch.asc(rootToSearch.get(order.getProperty()));
                orderList.add(orderItem);
            }
            else if (order.getDirection().equals(Sort.Direction.DESC)) {
                Order orderItem = criteriaBuilderToSearch.desc(rootToSearch.get(order.getProperty()));
                orderList.add(orderItem);
            }
        });
        criteriaQueryToSearch.orderBy(orderList);
        predicateList = PersistenceUtil.toPredicates(filterParamsMap, criteriaBuilderToSearch, rootToSearch, queryClass);
        Iterator<Map.Entry<String, Object>> requiredParamMapIterator = requiredParamsMap.entrySet().iterator();
        while (requiredParamMapIterator.hasNext()) {
            Map.Entry<String, Object> entry = requiredParamMapIterator.next();
            requiredParamPredicate = criteriaBuilderToSearch.equal(PersistenceUtil.getPath(rootToSearch, entry.getKey()), entry.getValue());
            predicateList.add(requiredParamPredicate);
        }

        Predicate predicatesToSearch = criteriaBuilderToSearch.and(predicateList.toArray(new Predicate[predicateList.size()]));
        criteriaQueryToSearch.where(predicatesToSearch);
        List<T> resultList = entityManager.createQuery(criteriaQueryToSearch.select(rootToSearch))
            .setMaxResults(size)
            .setFirstResult(page * size)
            .getResultList();

        return resultList;
    }
}

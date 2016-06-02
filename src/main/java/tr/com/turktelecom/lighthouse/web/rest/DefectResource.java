package tr.com.turktelecom.lighthouse.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.domain.Severity;
import tr.com.turktelecom.lighthouse.repository.DefectRepository;
import tr.com.turktelecom.lighthouse.repository.search.DefectSearchRepository;
import tr.com.turktelecom.lighthouse.service.DefectService;
import tr.com.turktelecom.lighthouse.service.util.PersistenceUtil;
import tr.com.turktelecom.lighthouse.web.rest.dto.DefectDTO;
import tr.com.turktelecom.lighthouse.web.rest.mapper.DefectMapper;
import tr.com.turktelecom.lighthouse.web.rest.util.HeaderUtil;
import tr.com.turktelecom.lighthouse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * REST controller for managing Defect.
 */
@RestController
@RequestMapping("/api")
public class DefectResource {

    private final Logger log = LoggerFactory.getLogger(DefectResource.class);

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private DefectSearchRepository defectSearchRepository;

    @Inject
    private DefectService defectService;

    @Autowired
    private DefectMapper defectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * POST  /defects -> Create a new defect.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DefectDTO> createDefect(@RequestBody DefectDTO defectDTO) throws URISyntaxException {
        log.debug("REST request to save Defect : {}", defectDTO);
        if (defectDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("defect", "idexists", "A new defect cannot already have an ID")).body(null);
        }
        DefectDTO newDefectDTO = defectService.createDefect(defectDTO);
        return ResponseEntity.created(new URI("/api/defects/" + newDefectDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("defect", newDefectDTO.getId().toString()))
            .body(newDefectDTO);
    }

    /**
     * PUT  /defects -> Updates an existing defect.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DefectDTO> updateDefect(@RequestBody DefectDTO defectDTO) throws URISyntaxException {

        log.debug("REST request to update Defect : {}", defectDTO);
        if (defectDTO.getId() == null) {
            return createDefect(defectDTO);
        }

        return Optional.ofNullable(defectDTO).map(d -> {
           DefectDTO updatedDefectDTO = defectService.updateDefect(defectDTO);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("defect", updatedDefectDTO.getId().toString()))
                .body(defectDTO);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

//    /**
//     * GET  /defects -> get all the defects.
//     */
//    @RequestMapping(value = "/defects",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
    public ResponseEntity<List<DefectDTO>> getAllDefects(Pageable pageable, Long scanId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Defects");
        Page<Defect> page = defectRepository.findAllByScanId(pageable, scanId);
        List<DefectDTO> defectDTOList = page.getContent().stream().map(
            defect -> defectMapper.defectToDefectDTO(defect)
        ).collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/defects");
        return new ResponseEntity<>(defectDTOList, headers, HttpStatus.OK);
    }

    /**
     * GET  /defects -> search defects.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DefectDTO>> searchDefects(Pageable pageable, @RequestParam Long scanId, @RequestParam Map<String,String> filterParams)
        throws URISyntaxException {

        if (scanId == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("defect", "noScanId", "Scan ID must be specified")).body(null);
        }

        Map<String, String> responseParams = PaginationUtil.getOnlyFilteringParameters(filterParams, "scanId");
        if (StringUtils.isEmpty(responseParams.get("filterParams"))) {
            return getAllDefects(pageable, scanId);
        }

        log.debug("REST request to search Defects");
        CriteriaBuilder criteriaBuilderToCount = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQueryToCount = criteriaBuilderToCount.createQuery(Long.class);
        Root<Defect> rootToCount = criteriaQueryToCount.from(Defect.class);
        responseParams = PaginationUtil.extractFilterParams(responseParams);

        Integer page = Optional.ofNullable(pageable.getPageNumber()).map(Integer::valueOf).orElse(0);
        Integer size = Optional.ofNullable(pageable.getPageSize()).map(Integer::valueOf).orElse(10);
        Sort sort = Optional.ofNullable(pageable.getSort()).orElse(new Sort("id"));

        Map<String, Class> enumParameters = new HashMap<String, Class>();
        enumParameters.put("severity", Severity.class);
        List<Predicate> predicateList = PersistenceUtil.toPredicates(responseParams, criteriaBuilderToCount, rootToCount, enumParameters);
        Predicate scanIdPredicate = criteriaBuilderToCount.equal(PersistenceUtil.getPath(Long.class, rootToCount, "scan.id"), scanId);
        predicateList.add(scanIdPredicate);
        Predicate predicatesToCount = criteriaBuilderToCount.and(predicateList.toArray(new Predicate[predicateList.size()]));
        criteriaQueryToCount.select(criteriaBuilderToCount.count(rootToCount));
        criteriaQueryToCount.where(predicatesToCount);
        Long count = entityManager.createQuery(criteriaQueryToCount).getSingleResult();

        List<DefectDTO> defectDTOList = null;
        if(count>0){
            CriteriaBuilder criteriaBuilderToSearch = entityManager.getCriteriaBuilder();
            CriteriaQuery<Defect> criteriaQueryToSearch = criteriaBuilderToSearch.createQuery(Defect.class);
            Root<Defect> rootToSearch = criteriaQueryToSearch.from(Defect.class);
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
            predicateList = PersistenceUtil.toPredicates(responseParams, criteriaBuilderToSearch, rootToSearch, enumParameters);
            scanIdPredicate = criteriaBuilderToSearch.equal(PersistenceUtil.getPath(Long.class, rootToSearch, "scan.id"), scanId);
            predicateList.add(scanIdPredicate);

            Predicate predicatesToSearch = criteriaBuilderToSearch.and(predicateList.toArray(new Predicate[predicateList.size()]));
            criteriaQueryToSearch.where(predicatesToSearch);
            defectDTOList = entityManager.createQuery(criteriaQueryToSearch.select(rootToSearch))
                .setMaxResults(size)
                .setFirstResult(page * size)
                .getResultList().stream().map(defect -> {
                    return defectMapper.defectToDefectDTO(defect);
                }).collect(Collectors.toList());
        }else {
            defectDTOList = new ArrayList<DefectDTO>(0);
        }


        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(new PageImpl<DefectDTO>(defectDTOList, new PageRequest(page, size, sort), count), "/api/_search/defects/" + scanId, responseParams);
        return new ResponseEntity<>(defectDTOList, headers, HttpStatus.OK);
    }

    /**
     * GET  /defects/:id -> get the "id" defect.
     */
    @RequestMapping(value = "/defects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DefectDTO> getDefect(@PathVariable Long id) {
        log.debug("REST request to get Defect : {}", id);

        return defectRepository.findOneById(id)
            .map(defect -> defectMapper.defectToDefectDTO(defect))
            .map(defectDTO -> new ResponseEntity<>(defectDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /defects/:id -> delete the "id" defect.
     */
    @RequestMapping(value = "/defects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDefect(@PathVariable Long id) {
        log.debug("REST request to delete Defect : {}", id);
        defectRepository.delete(id);
        defectSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("defect", id.toString())).build();
    }

//    /**
//     * SEARCH  /_search/defects/:query -> search for the defect corresponding
//     * to the query.
//     */
//    @RequestMapping(value = "/_search/defects",
//        method = RequestMethod.POST,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public List<DefectDTO> searchDefects(@RequestBody DefectSearchDTO defectSearchDTO) {
//        log.debug("REST request to search Defects for query {}", defectSearchDTO);
//        Pageable pageable = new PageRequest(defectSearchDTO.getPage(), defectSearchDTO.getSize(), new Sort(defectSearchDTO.getSort()));
//        return StreamSupport.stream(
//                defectSearchRepository.searchSimilar(
//                    defectMapper.defectDTOToDefect(defectSearchDTO), null, pageable
//                ).spliterator(), false)
//            .map(defect -> {
//                return defectMapper.defectToDefectDTO(defect);
//            })
//            .collect(Collectors.toList());
//        return null;
//    }


//    /**
//     * POST  /defects -> Search a defect using specifications.
//     */
//    @RequestMapping(value = "/_search/defects/{scanId}",
//        method = RequestMethod.GET,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<List<DefectDTO>> findDefects(@PathVariable Long scanId, @RequestParam Map<String,String> allRequestParams, ModelMap model) throws URISyntaxException{
//        Map<String, String> responseParams = new HashMap<String, String>();
//
//        CriteriaBuilder criteriaBuilderToCount = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Long> criteriaQueryToCount = criteriaBuilderToCount.createQuery(Long.class);
//        Root<Defect> rootToCount = criteriaQueryToCount.from(Defect.class);
//
//        if (scanId == null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("defect", "noScanId", "Scan ID must be specified")).body(null);
//        }
//
//        Integer page = Optional.ofNullable(allRequestParams.get("page")).map(Integer::valueOf).orElse(0);
//        Integer size = Optional.ofNullable(allRequestParams.get("size")).map(Integer::valueOf).orElse(10);
//        Sort sort = Optional.ofNullable(allRequestParams.get("sort")).map(s -> new Sort(s)).orElse(new Sort("id"));
//
//        Optional.ofNullable(allRequestParams.get("search")).map(search -> {
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                JsonNode searchPredicate = mapper.readTree(search);
//                Iterator<String> fieldNames = searchPredicate.fieldNames();
//                while (fieldNames.hasNext()) {
//                    String field = fieldNames.next();
//                    responseParams.put(field, searchPredicate.get(field).textValue());
//                }
//            } catch (IOException e) {
//                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("defect", "badSearchPredicate", "Bad search predicate object properties")).body(null);
//            }
//            return null;
//        });
//
//
//
//        List<Predicate> predicateList = PersistenceUtil.toPredicates(responseParams, criteriaBuilderToCount, rootToCount);
//        Predicate scanIdPredicate = criteriaBuilderToCount.equal(PersistenceUtil.getPath(Long.class, rootToCount, "scan.id"), scanId);
//        predicateList.add(scanIdPredicate);
//        Predicate predicatesToCount = criteriaBuilderToCount.and(predicateList.toArray(new Predicate[predicateList.size()]));
//        criteriaQueryToCount.select(criteriaBuilderToCount.count(rootToCount));
//        criteriaQueryToCount.where(predicatesToCount);
//        Long count = entityManager.createQuery(criteriaQueryToCount).getSingleResult();
//
//        List<DefectDTO> defectDTOList = null;
//        if(count>0){
//            CriteriaBuilder criteriaBuilderToSearch = entityManager.getCriteriaBuilder();
//            CriteriaQuery<Defect> criteriaQueryToSearch = criteriaBuilderToSearch.createQuery(Defect.class);
//            Root<Defect> rootToSearch = criteriaQueryToSearch.from(Defect.class);
//            //criteriaBuilderToSearch.equal(PersistenceUtil.getPath(Long.class, rootToSearch, "scan.id"), searchDTO.getScanId());
//            predicateList = PersistenceUtil.toPredicates(responseParams, criteriaBuilderToSearch, rootToSearch);
//            scanIdPredicate = criteriaBuilderToSearch.equal(PersistenceUtil.getPath(Long.class, rootToSearch, "scan.id"), scanId);
//            predicateList.add(scanIdPredicate);
//
//
//
//            Predicate predicatesToSearch = criteriaBuilderToSearch.and(predicateList.toArray(new Predicate[predicateList.size()]));
//            criteriaQueryToSearch.where(predicatesToSearch);
//            defectDTOList = entityManager.createQuery(criteriaQueryToSearch.select(rootToSearch))
//                .setMaxResults(size)
//                .setFirstResult(page * size)
//                .getResultList().stream().map(defect -> {
//                return defectMapper.defectToDefectDTO(defect);
//            }).collect(Collectors.toList());
//        }else {
//            defectDTOList = new ArrayList<DefectDTO>(0);
//        }
//
//
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(new PageImpl<DefectDTO>(defectDTOList, new PageRequest(0, size, sort), count), "/api/_search/defects/" + scanId, responseParams);
//        return new ResponseEntity<>(defectDTOList, headers, HttpStatus.OK);
//    }
//
}

package tr.com.turktelecom.lighthouse.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.repository.DefectRepository;
import tr.com.turktelecom.lighthouse.repository.search.DefectSearchRepository;
import tr.com.turktelecom.lighthouse.service.DefectService;
import tr.com.turktelecom.lighthouse.service.search.SearchCriteria;
import tr.com.turktelecom.lighthouse.service.search.SearchSpecification;
import tr.com.turktelecom.lighthouse.service.search.SearchSpecificationsBuilder;
import tr.com.turktelecom.lighthouse.web.rest.dto.DefectDTO;
import tr.com.turktelecom.lighthouse.web.rest.dto.search.DefectSearchDTO;
import tr.com.turktelecom.lighthouse.web.rest.mapper.DefectMapper;
import tr.com.turktelecom.lighthouse.web.rest.util.HeaderUtil;
import tr.com.turktelecom.lighthouse.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

    /**
     * GET  /defects -> get all the defects.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DefectDTO>> getAllDefects(Pageable pageable, @RequestParam Long scanId)
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

    /**
     * POST  /defects -> Search a defect using specifications.
     */
    @RequestMapping(value = "/_search/defects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DefectDTO>> findDefects(@RequestBody DefectSearchDTO defectSearchDTO) throws URISyntaxException{
        Pageable pageable = new PageRequest(defectSearchDTO.getPage(), defectSearchDTO.getSize(), new Sort(defectSearchDTO.getSort()));
        if (defectSearchDTO.getSpecifications() == null || defectSearchDTO.getSpecifications().length == 0) {
            return getAllDefects(pageable, defectSearchDTO.getScanId());
        }

        SearchSpecificationsBuilder<Defect> builder = new SearchSpecificationsBuilder();
        for (String search : defectSearchDTO.getSpecifications()) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }

        Specification<Defect> spec = builder.build();
        Page<Defect> page = defectRepository.findAll(spec, pageable);
        List<DefectDTO> defectDTOList = page.getContent().stream().map(defect -> {
            return defectMapper.defectToDefectDTO(defect);
        }).collect(Collectors.toList());

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/defects");
        return new ResponseEntity<>(defectDTOList, headers, HttpStatus.OK);
    }
}

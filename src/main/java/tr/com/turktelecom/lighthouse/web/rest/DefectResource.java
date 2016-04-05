package tr.com.turktelecom.lighthouse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.repository.DefectRepository;
import tr.com.turktelecom.lighthouse.repository.search.DefectSearchRepository;
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

    /**
     * POST  /defects -> Create a new defect.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Defect> createDefect(@RequestBody Defect defect) throws URISyntaxException {
        log.debug("REST request to save Defect : {}", defect);
        if (defect.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("defect", "idexists", "A new defect cannot already have an ID")).body(null);
        }
        Defect result = defectRepository.save(defect);
        defectSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/defects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("defect", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /defects -> Updates an existing defect.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Defect> updateDefect(@RequestBody Defect defect) throws URISyntaxException {
        log.debug("REST request to update Defect : {}", defect);
        if (defect.getId() == null) {
            return createDefect(defect);
        }
        Defect result = defectRepository.save(defect);
        defectSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("defect", defect.getId().toString()))
            .body(result);
    }

    /**
     * GET  /defects -> get all the defects.
     */
    @RequestMapping(value = "/defects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Defect>> getAllDefects(Pageable pageable, @RequestParam Long scanId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Defects");
        Page<Defect> page = defectRepository.findAllByScanId(pageable, scanId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/defects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /defects/:id -> get the "id" defect.
     */
    @RequestMapping(value = "/defects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Defect> getDefect(@PathVariable Long id) {
        log.debug("REST request to get Defect : {}", id);
        Defect defect = defectRepository.findOne(id);
        return Optional.ofNullable(defect)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
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

    /**
     * SEARCH  /_search/defects/:query -> search for the defect corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/defects/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Defect> searchDefects(@PathVariable String query) {
        log.debug("REST request to search Defects for query {}", query);
        return StreamSupport
            .stream(defectSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

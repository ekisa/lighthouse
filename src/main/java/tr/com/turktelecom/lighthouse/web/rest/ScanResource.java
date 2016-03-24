package tr.com.turktelecom.lighthouse.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.com.turktelecom.lighthouse.domain.Scan;
import tr.com.turktelecom.lighthouse.repository.ScanRepository;
import tr.com.turktelecom.lighthouse.repository.search.ScanSearchRepository;
import tr.com.turktelecom.lighthouse.web.rest.util.HeaderUtil;
import tr.com.turktelecom.lighthouse.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Scan.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class ScanResource {

    private final Logger log = LoggerFactory.getLogger(ScanResource.class);

    @Inject
    private ScanRepository scanRepository;

    @Inject
    private ScanSearchRepository scanSearchRepository;

    /**
     * POST  /scans -> Create a new scan.
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> createScan(@PathVariable String projectId, @RequestBody Scan scan) throws URISyntaxException {
        log.debug("REST request to save Scan : {}", scan);
        if (scan.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scan", "idexists", "A new scan cannot already have an ID")).body(null);
        }
        Scan result = scanRepository.save(scan);
        scanSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/scans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scan", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scans -> Updates an existing scan.
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> updateScan(@PathVariable String projectId, @RequestBody Scan scan) throws URISyntaxException {
        log.debug("REST request to update Scan : {}", scan);
        if (scan.getId() == null) {
            return createScan(projectId, scan);
        }
        Scan result = scanRepository.save(scan);
        scanSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scan", scan.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scans -> get all the scans.
     */
    @RequestMapping(value = "/scans",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Scan>> getAllScans(@PathVariable String projectId, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Scans");
        Page<Scan> page = scanRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scans");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scans/:id -> get the "id" scan.
     */
    @RequestMapping(value = "/scans/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Scan> getScan(@PathVariable String projectId, @PathVariable Long id) {
        log.debug("REST request to get Scan : {}", id);
        Scan scan = scanRepository.findOne(id);
        return Optional.ofNullable(scan)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /scans/:id -> delete the "id" scan.
     */
    @RequestMapping(value = "/scans/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteScan(@PathVariable String projectId, @PathVariable Long id) {
        log.debug("REST request to delete Scan : {}", id);
        scanRepository.delete(id);
        scanSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scan", id.toString())).build();
    }

    /**
     * SEARCH  /_search/scans/:query -> search for the scan corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/scans/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Scan> searchScans(@PathVariable String projectId, @PathVariable String query) {
        log.debug("REST request to search Scans for query {}", query);
        return StreamSupport
            .stream(scanSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

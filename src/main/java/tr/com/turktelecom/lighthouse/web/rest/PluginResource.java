package tr.com.turktelecom.lighthouse.web.rest;

import com.codahale.metrics.annotation.Timed;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
import tr.com.turktelecom.lighthouse.repository.search.PluginSearchRepository;
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
 * REST controller for managing Plugin.
 */
@RestController
@RequestMapping("/api")
public class PluginResource {

    private final Logger log = LoggerFactory.getLogger(PluginResource.class);

    @Inject
    private PluginRepository pluginRepository;

    @Inject
    private PluginSearchRepository pluginSearchRepository;

    /**
     * POST  /plugins -> Create a new plugin.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plugin> createPlugin(@RequestBody Plugin plugin) throws URISyntaxException {
        log.debug("REST request to save Plugin : {}", plugin);
        if (plugin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("plugin", "idexists", "A new plugin cannot already have an ID")).body(null);
        }
        Plugin result = pluginRepository.save(plugin);
        pluginSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/plugins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("plugin", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /plugins -> Updates an existing plugin.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plugin> updatePlugin(@RequestBody Plugin plugin) throws URISyntaxException {
        log.debug("REST request to update Plugin : {}", plugin);
        if (plugin.getId() == null) {
            return createPlugin(plugin);
        }
        Plugin result = pluginRepository.save(plugin);
        pluginSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("plugin", plugin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /plugins -> get all the plugins.
     */
    @RequestMapping(value = "/plugins",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Plugin>> getAllPlugins(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Plugins");
        Page<Plugin> page = pluginRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/plugins");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /plugins/:id -> get the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Plugin> getPlugin(@PathVariable Long id) {
        log.debug("REST request to get Plugin : {}", id);
        Plugin plugin = pluginRepository.findOne(id);
        return Optional.ofNullable(plugin)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /plugins/:id -> delete the "id" plugin.
     */
    @RequestMapping(value = "/plugins/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlugin(@PathVariable Long id) {
        log.debug("REST request to delete Plugin : {}", id);
        pluginRepository.delete(id);
        pluginSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("plugin", id.toString())).build();
    }

    /**
     * SEARCH  /_search/plugins/:query -> search for the plugin corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/plugins/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Plugin> searchPlugins(@PathVariable String query) {
        log.debug("REST request to search Plugins for query {}", query);
        return StreamSupport
            .stream(pluginSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}

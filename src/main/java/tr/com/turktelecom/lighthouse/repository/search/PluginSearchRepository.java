package tr.com.turktelecom.lighthouse.repository.search;

import tr.com.turktelecom.lighthouse.domain.Plugin;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Plugin entity.
 */
public interface PluginSearchRepository extends ElasticsearchRepository<Plugin, Long> {
}

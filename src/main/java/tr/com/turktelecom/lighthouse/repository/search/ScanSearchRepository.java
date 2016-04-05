package tr.com.turktelecom.lighthouse.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import tr.com.turktelecom.lighthouse.domain.Scan;

/**
 * Spring Data ElasticSearch repository for the Scan entity.
 */
public interface ScanSearchRepository extends ElasticsearchRepository<Scan, Long> {
}

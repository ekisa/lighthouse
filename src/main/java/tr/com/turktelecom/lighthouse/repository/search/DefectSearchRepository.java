package tr.com.turktelecom.lighthouse.repository.search;

import tr.com.turktelecom.lighthouse.domain.Defect;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Defect entity.
 */
public interface DefectSearchRepository extends ElasticsearchRepository<Defect, Long> {
}

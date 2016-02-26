package tr.com.turktelecom.lighthouse.repository.search;

import tr.com.turktelecom.lighthouse.domain.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Project entity.
 */
public interface ProjectSearchRepository extends ElasticsearchRepository<Project, Long> {
}

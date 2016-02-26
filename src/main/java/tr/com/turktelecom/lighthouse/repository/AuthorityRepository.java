package tr.com.turktelecom.lighthouse.repository;

import tr.com.turktelecom.lighthouse.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}

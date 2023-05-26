package jparest.practice.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRepository extends JpaRepository<String, RestRepository> {
}

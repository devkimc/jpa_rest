package jparest.practice.rest.repository;

import jparest.practice.rest.domain.Rest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestRepository extends JpaRepository<Rest, String> {
}

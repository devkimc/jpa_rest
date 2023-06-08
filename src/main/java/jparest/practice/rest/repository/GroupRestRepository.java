package jparest.practice.rest.repository;

import jparest.practice.rest.domain.GroupRest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRestRepository extends JpaRepository<GroupRest, Long> {

    Optional<GroupRest> findByGroupIdAndRestId(Long groupId, String groupRestId);

}

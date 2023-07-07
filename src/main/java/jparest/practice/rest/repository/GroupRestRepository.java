package jparest.practice.rest.repository;

import jparest.practice.rest.domain.GroupRest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupRestRepository extends JpaRepository<GroupRest, Long> {

    @Query("SELECT gr FROM GroupRest gr WHERE gr.group.id = :groupId AND gr.rest.id = :restId")
    Optional<GroupRest> findByGroupIdAndRestId(@Param("groupId") Long groupId, @Param("restId") String restId);
}

package jparest.practice.group.repository;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    Optional<UserGroup> findByUserIdAndGroupId(UUID userId, Long groupId);
}


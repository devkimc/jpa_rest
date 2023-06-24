package jparest.practice.group.repository;

import jparest.practice.group.domain.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    Optional<GroupUser> findByUserIdAndGroupId(UUID userId, Long groupId);

    Optional<List<GroupUser>> findAllByGroupId(Long groupId);
}


package jparest.practice.group.repository;

import jparest.practice.group.domain.GroupUser;
import jparest.practice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    @Query("SELECT gu FROM GroupUser gu WHERE gu.user = :user AND gu.group.id = :groupId")
    Optional<GroupUser> findByUserAndGroupId(@Param("user") User user, @Param("groupId") Long groupId);

    @Query("SELECT gu FROM GroupUser gu WHERE gu.group.id = :groupId")
    Optional<List<GroupUser>> findAllByGroupId(@Param("groupId") Long groupId);
}


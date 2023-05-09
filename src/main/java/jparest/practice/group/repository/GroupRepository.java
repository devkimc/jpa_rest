package jparest.practice.group.repository;

import jparest.practice.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GroupRepository extends JpaRepository<Group, Long> {

//    @Query("select g from GroupMember g where g.groupId = :groupId and :g.memberId = :memberId")
//    GroupMember findByGroupIdAndMemberId(@Param("groupId") Long groupId,
//                                         @Param("memberId") Long memberId);
}

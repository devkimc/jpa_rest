package jparest.practice.group.repository;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GroupRepository {

    private final EntityManager em;

    public void save(Group group) {
        if (group.getId() == null) {
            em.persist(group);
        } else {
            em.merge(group);
        }
    }

    public Group findOne(Long id) {
        return em.find(Group.class, id);
    }

    public GroupMember findByGroupIdAndMemberId(Long groupId, Long memberId) {
        return em.createQuery("select g from GroupMember g where g.groupId = :groupId and :g.memberId = :memberId",
                        GroupMember.class)
                .setParameter("groupId", groupId)
                .setParameter("memberID", memberId)
                .getSingleResult();
    }

}

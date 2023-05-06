package jparest.practice.service;

import jparest.practice.group.domain.Group;
import jparest.practice.member.domain.Member;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.invite.repository.InviteRepository;
import jparest.practice.invite.service.InviteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InviteServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    InviteService inviteService;
    @Autowired
    InviteRepository inviteRepository;

    @Autowired
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;

    @Test
    public void 그룹_초대() throws Exception {

        //given
        Member member = createMember();
        Group group = createGroup(member.getId());
        System.out.println("group = " + group.getGroupMembers());

        //when

        //then
    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("회원1");
        em.persist(member);
        return member;
    }

    private Group createGroup(Long memberId) {
        Group group = new Group();
        group.setGroupName("반민초파");
        group.setCreateUserId(memberId);
        em.persist(group);
        return group;
    }
}

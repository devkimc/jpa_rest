package jparest.practice.service;

import jparest.practice.domain.Group;
import jparest.practice.domain.Member;
import jparest.practice.repository.GroupRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
public class GroupServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired GroupService groupService;
    @Autowired
    GroupRepository groupRepository;

    @Test
    public void 그룹생성() throws Exception {

        //given
        Member member = createMember();

        //when
        Long groupId = groupService.makeGroup(member.getId(), "반민초파");

        //then
        Group getGroup = groupRepository.findOne(groupId);

        Assert.assertEquals("생성한 그룹 수가 정확해야 한다.",
                1, getGroup.getGroupMembers().size());

        Assert.assertEquals("그룹 생성자가 정확해야 한다..",
                member.getId(), getGroup.getCreateUserId());

    }

    private Member createMember() {
        Member member = new Member();
        member.setUsername("회원1");
        em.persist(member);
        return member;
    }

}

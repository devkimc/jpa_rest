package jparest.practice.subscription.service;

import jparest.practice.common.fixture.SubscriptionFixture;
import jparest.practice.group.domain.Group;
import jparest.practice.group.dto.CreateGroupRequest;
import jparest.practice.group.dto.CreateGroupResponse;
import jparest.practice.group.exception.ExistGroupUserException;
import jparest.practice.group.exception.GroupNotFoundException;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.group.service.GroupService;
import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.exception.ExistWaitingSubscriptionException;
import jparest.practice.subscription.repository.SubscriptionRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.createFirstUser;
import static jparest.practice.common.fixture.UserFixture.createSecondUser;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {

    private User firstUser;
    private User secondUser;
    private Group group;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        firstUser = userAuthService.join(createFirstUser());
        secondUser = userAuthService.join(createSecondUser());

        CreateGroupRequest createGroupRequest = CreateGroupRequest.builder()
                .groupName(groupName1)
                .isPublic(true)
                .build();

        CreateGroupResponse response = groupService.createGroup(firstUser, createGroupRequest);
        group = findGroupById(response.getId());
    }

    @Test
    public void 가입한_그룹에_가입신청시_에러가_발생한다() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message)
                .build();

        //when
        //then
        Assertions.assertThrows(ExistGroupUserException.class,
                () -> subscriptionService.subscribeForGroup(firstUser, subscribeForGroupRequest));
    }


    @Test
    public void 대기중인_가입신청이_존재할때_가입신청시_에러가_발생한다() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message)
                .build();

        //when
        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        //then
        Assertions.assertThrows(ExistWaitingSubscriptionException.class,
                () -> subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest));
    }


    @Test
    public void 가입신청_시() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message)
                .build();

        //when
        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        Subscription subscription = group.getSubscriptions().get(0);

        //then
        assertAll(
                () -> assertEquals(secondUser, subscription.getApplicant(), "신청한 유저가 동일해야 한다."),
                () -> assertEquals(group, subscription.getGroup(), "신청한 그룹이 동일해야 한다."),
                () -> assertEquals(SubscriptionFixture.message, subscription.getMessage(), "신청 메시지가 동일해야 한다.")
        );
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }
}
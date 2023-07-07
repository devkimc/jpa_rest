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
import jparest.practice.subscription.dto.GetReceivedSubscriptionResponse;
import jparest.practice.subscription.dto.ProcessSubscriptionRequest;
import jparest.practice.subscription.dto.SubscribeForGroupRequest;
import jparest.practice.subscription.exception.ExistWaitingSubscriptionException;
import jparest.practice.user.domain.User;
import jparest.practice.user.service.UserAuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static jparest.practice.common.fixture.GroupFixture.groupName1;
import static jparest.practice.common.fixture.UserFixture.*;
import static jparest.practice.subscription.domain.SubscriptionStatus.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class SubscriptionServiceTest {

    private User firstUser;
    private User secondUser;
    private User thirdUser;
    private Group group;

    @Autowired
    UserAuthService userAuthService;

    @Autowired
    GroupService groupService;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    SubscriptionService subscriptionService;

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
                .message(SubscriptionFixture.message1)
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
                .message(SubscriptionFixture.message1)
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
                .message(SubscriptionFixture.message1)
                .build();

        //when
        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        Subscription subscription = group.getSubscriptions().get(0);

        //then
        assertAll(
                () -> assertEquals(secondUser, subscription.getApplicant(), "신청한 유저가 동일해야 한다."),
                () -> assertEquals(group, subscription.getGroup(), "신청한 그룹이 동일해야 한다."),
                () -> assertEquals(SubscriptionFixture.message1, subscription.getMessage(), "신청 메시지가 동일해야 한다.")
        );
    }

    @Test
    public void 가입신청을_그룹원이_수락_시() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message1)
                .build();

        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        Subscription subscription = group.getSubscriptions().get(0);

        ProcessSubscriptionRequest request = ProcessSubscriptionRequest.builder().status(ACCEPT).build();

        //when
        subscriptionService.processSubscription(firstUser, subscription.getId(), request);

        //then
        assertAll(
                () -> assertEquals(true, secondUser.isJoinGroup(group), "가입신청한 유저는 그룹원이 된다."),
                () -> assertEquals(ACCEPT, subscription.getStatus(), "가입 신청 상태는 수락상태로 변경된다.")
        );
    }

    @Test
    public void 가입신청을_그룹원이_거절_시() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message1)
                .build();

        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        Subscription subscription = group.getSubscriptions().get(0);

        ProcessSubscriptionRequest request = ProcessSubscriptionRequest.builder().status(REJECT).build();

        //when
        subscriptionService.processSubscription(firstUser, subscription.getId(), request);

        //then
        assertAll(
                () -> assertEquals(false, secondUser.isJoinGroup(group),
                        "가입신청한 유저는 그룹원이 되지 않는다."),
                () -> assertEquals(REJECT, subscription.getStatus(), "가입 신청 상태는 거절상태로 변경된다.")
        );
    }

    @Test
    public void 가입신청을_신청한_유저가_취소_시() throws Exception {

        //given
        SubscribeForGroupRequest subscribeForGroupRequest = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message1)
                .build();

        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest);

        Subscription subscription = group.getSubscriptions().get(0);

        ProcessSubscriptionRequest request = ProcessSubscriptionRequest.builder().status(CANCEL).build();

        //when
        subscriptionService.processSubscription(secondUser, subscription.getId(), request);

        //then
        assertAll(
                () -> assertEquals(false, secondUser.isJoinGroup(group),
                        "가입신청한 유저는 그룹원이 되지 않는다."),
                () -> assertEquals(CANCEL, subscription.getStatus(), "가입 신청 상태는 거절상태로 변경된다.")
        );
    }

    @Test
    public void 그룹원이_가입신청내역_조회_시() throws Exception {

        //given
        thirdUser = userAuthService.join(createThirdUser());

        SubscribeForGroupRequest subscribeForGroupRequest1 = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message1)
                .build();

        SubscribeForGroupRequest subscribeForGroupRequest2 = SubscribeForGroupRequest.builder()
                .groupId(group.getId())
                .message(SubscriptionFixture.message2)
                .build();

        subscriptionService.subscribeForGroup(secondUser, subscribeForGroupRequest1);
        subscriptionService.subscribeForGroup(thirdUser, subscribeForGroupRequest2);

        //when
        List<GetReceivedSubscriptionResponse> receivedSubscription =
                subscriptionService.getReceivedSubscription(firstUser, group.getId());

        //then
        assertAll(
                () -> assertEquals(2, receivedSubscription.size(), "리스트 수가 동일하다.")
        );
    }

    private Group findGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new GroupNotFoundException("groupId = " + groupId));
    }
}
package jparest.practice.subscription.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.Group;
import jparest.practice.group.exception.GroupAccessDeniedException;
import jparest.practice.subscription.exception.SubscriptionNotFoundException;
import jparest.practice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static jparest.practice.subscription.domain.SubscriptionStatus.*;
@Table(name = "group_subscription")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User applicant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Column(length = 40)
    private String message;

    public void updateStatus(SubscriptionStatus status) {
        this.status = status;
    }

    //==생성 메서드==//
    public static Subscription createSubscription(User applicant, Group group, String message) {
        Subscription subscription = Subscription.builder()
                .group(group)
                .applicant(applicant)
                .message(message)
                .status(WAITING)
                .build();

        applicant.getSubscriptions().add(subscription);
        group.getSubscriptions().add(subscription);

        return subscription;
    }

    public void chkAuthorizationOfSubscriptionProcess(User user, SubscriptionStatus status) {

        String strSubscriptionIdAndUserId = "subscriptionId = " + this.id + ", userId = " + user.getId();

        if (status == ACCEPT && !user.isJoinGroup(this.group)) {
            throw new GroupAccessDeniedException(
                    "가입신청을 승낙할 권리가 없습니다. " + strSubscriptionIdAndUserId + " groupId = " + this.group.getId());
        }

        if (status == REJECT && !user.isJoinGroup(this.group)) {
            throw new GroupAccessDeniedException(
                    "가입신청을 거절할 권리가 없습니다. " + strSubscriptionIdAndUserId + " groupId = " + this.group.getId());
        }

        if (status == CANCEL && !user.equals(this.applicant)) {
            throw new SubscriptionNotFoundException("취소 요청한 유저의 가입 신청이 아닙니다. " + strSubscriptionIdAndUserId);
        }
    }
}

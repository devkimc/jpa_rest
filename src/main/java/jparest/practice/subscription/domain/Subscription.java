package jparest.practice.subscription.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.Group;
import jparest.practice.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "group_subscription")
@Entity
@Getter
@NoArgsConstructor
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

    public Subscription(Group group, User applicant, String message, SubscriptionStatus status) {
        this.group = group;
        this.applicant = applicant;
        this.message = message;
        this.status = status;
    }

    //==생성 메서드==//
    public static Subscription createSubscription(User applicant, Group group, String message) {
        Subscription subscription = new Subscription(group, applicant, message, SubscriptionStatus.WAITING);
        applicant.getSubscriptions().add(subscription);
        group.getSubscriptions().add(subscription);
        return subscription;
    }
}

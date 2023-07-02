package jparest.practice.subscription.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.Group;
import jparest.practice.user.domain.User;
import lombok.Getter;

import javax.persistence.*;

@Table(name = "group_subscription")
@Entity
@Getter
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
    private SubscriptionStatus subscriptionStatus;
}

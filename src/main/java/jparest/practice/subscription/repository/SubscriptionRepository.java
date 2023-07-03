package jparest.practice.subscription.repository;

import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.domain.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByApplicantIdAndGroupIdAndStatus(UUID applicantId,
                                                                Long groupId,
                                                                SubscriptionStatus status);

}

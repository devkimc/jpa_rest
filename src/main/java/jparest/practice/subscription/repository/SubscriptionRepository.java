package jparest.practice.subscription.repository;

import jparest.practice.subscription.domain.Subscription;
import jparest.practice.subscription.domain.SubscriptionStatus;
import jparest.practice.subscription.dto.GetReceivedSubscriptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query("SELECT s FROM Subscription s " +
            "WHERE s.applicant.id = :applicantId AND s.group.id = :groupId AND status = :status")
    Optional<Subscription> findByApplicantIdAndGroupIdAndStatus(@Param("applicantId") UUID applicantId,
                                                                @Param("groupId") Long groupId,
                                                                @Param("status") SubscriptionStatus status);

    @Query("SELECT " +
            "NEW jparest.practice.subscription.dto.GetReceivedSubscriptionResponse( " +
            "s.id, s.applicant.id, s.applicant.nickname, s.message, s.createdAt " +
            ") " +
            "FROM Subscription s WHERE s.group.id = :groupId AND status = :status")
    List<GetReceivedSubscriptionResponse> findAllByGroupIdAndStatus(@Param("groupId") Long groupId,
                                                                    @Param("status") SubscriptionStatus status);
}

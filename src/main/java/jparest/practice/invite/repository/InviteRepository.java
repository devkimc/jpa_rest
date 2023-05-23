package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    @Query("select i from Invite i " +
            "where i.sendUserGroup.id = :sendUserGroupId and i.recvUser.id = :recvUserId and i.inviteStatus = :inviteStatus ")
    Optional<Invite> findBySendUserGroupIdAndRecvUserIdAndInviteStatus(@Param("sendUserGroupId") Long sendUserGroupId,
                                                                       @Param("recvUserId") UUID recvUserId,
                                                                       @Param("inviteStatus") InviteStatus inviteStatus);
}

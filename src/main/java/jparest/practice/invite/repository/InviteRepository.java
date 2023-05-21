package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {
    Optional<Invite> findBySendUserGroupIdAndRecvUserIdAndInviteStatus(Long sendUserGroupId, UUID recvUserId, InviteStatus inviteStatus);
}

package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findBySendGroupUserIdAndRecvUserIdAndInviteStatus(Long sendGroupUserId,
                                                                       UUID recvUserId,
                                                                       InviteStatus inviteStatus);

    Optional<List<Invite>> findAllByRecvUserIdAndInviteStatus(UUID recvUserId, InviteStatus inviteStatus);

}

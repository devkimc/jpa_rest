package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    Optional<Invite> findBySendGroupUserIdAndRecvUserIdAndStatus(Long sendGroupUserId,
                                                                 UUID recvUserId,
                                                                 InviteStatus status);

    Optional<List<Invite>> findAllByRecvUserIdAndStatus(UUID recvUserId, InviteStatus status);

}

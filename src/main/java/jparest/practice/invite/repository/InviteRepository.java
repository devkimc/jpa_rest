package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import jparest.practice.invite.domain.InviteStatus;
import jparest.practice.invite.dto.GetWaitingInviteResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    @Query("SELECT i FROM Invite i " +
            "WHERE i.sendGroupUser.id = :sendGroupUserId AND i.recvUser.id = :recvUserId AND i.status = :status")
    Optional<Invite> findBySendGroupUserIdAndRecvUserIdAndStatus(@Param("sendGroupUserId") Long sendGroupUserId,
                                                                 @Param("recvUserId") UUID recvUserId,
                                                                 @Param("status") InviteStatus status);

    @Query("SELECT " +
            "NEW jparest.practice.invite.dto.GetWaitingInviteResponse ( " +
            "i.id, u.nickname, g.groupName ) " +
            "FROM Invite i " +
            "JOIN i.sendGroupUser gr " +
            "JOIN gr.user u " +
            "JOIN gr.group g " +
            "WHERE i.recvUser.id = :recvUserId AND i.status = :status")
    List<GetWaitingInviteResponse> findAllByRecvUserIdAndStatus(@Param("recvUserId") UUID recvUserId,
                                                                          @Param("status") InviteStatus status);
}

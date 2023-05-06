package jparest.practice.invite.repository;

import jparest.practice.invite.domain.Invite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class InviteRepository {

    private final EntityManager em;

    public void save(Invite invite) {
        if (invite.getId() == null) {
            em.persist(invite);
        } else {
            em.merge(invite);
        }
    }

    public List<Invite> findByRecvUserId(Long id) {
        return em.createQuery("select i from Invite i where i.id = :id",
                        Invite.class)
                .setParameter("id", id)
                .getResultList();
    }
}

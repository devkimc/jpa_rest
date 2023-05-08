package jparest.practice.user.repository;

import jparest.practice.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryV0 {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll() {
        return em.createQuery("select m from Member m", User.class)
                .getResultList();
    }

    public List<User> findByName(String username) {
        return em.createQuery("select m from Member m where m.username = :username",
                        User.class)
                .setParameter("username", username)
                .getResultList();
    }
}

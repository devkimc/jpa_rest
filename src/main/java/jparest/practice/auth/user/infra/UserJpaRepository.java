package jparest.practice.auth.user.infra;

import jparest.practice.auth.user.domain.User;
import jparest.practice.auth.user.domain.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {
}

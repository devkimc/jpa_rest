package jparest.practice.auth.user.domain;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findFirstUserByLoginIdOrderByIdAsc(String username);
    Optional<User> findById(long id);
    Optional<User> findByLoginId(String loginId);
    User save(User saveUser);
}
package jparest.practice.user.repository;

import jparest.practice.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findFirstUserByLoginIdOrderByIdAsc(String username);
    Optional<User> findById(long id);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findBySocialUserId(String socialUserId);
    User save(User saveUser);
}
package jparest.practice.member.repository;

import jparest.practice.member.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findFirstUserByLoginIdOrderByIdAsc(String username);
    Optional<User> findById(long id);
    Optional<User> findByLoginId(String loginId);
    User save(User saveUser);
}
package jparest.practice.user.repository;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.SocialJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findBySocialUserId(String socialUserId);
    User save(User user);

    Optional<User> findById(UUID userId);
}
package jparest.practice.member.repository;

import jparest.practice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long>, MemberRepository {
}

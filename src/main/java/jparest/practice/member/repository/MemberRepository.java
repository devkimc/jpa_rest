package jparest.practice.member.repository;

import jparest.practice.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findFirstUserByLoginIdOrderByIdAsc(String username);
    Optional<Member> findById(long id);
    Optional<Member> findByLoginId(String loginId);
    Member save(Member saveUser);
}
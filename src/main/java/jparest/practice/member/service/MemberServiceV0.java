package jparest.practice.member.service;

import jparest.practice.member.domain.User;
import jparest.practice.member.repository.MemberRepositoryV0;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceV0 {

    private final MemberRepositoryV0 memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(User user) {
        validateDuplicateMember(user);
        memberRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        List<User> findUsers = memberRepository.findByName(user.getName());
        if(!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findMembers() {
        return memberRepository.findAll();
    }

    public User findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

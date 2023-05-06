package jparest.practice.auth.security;

import jparest.practice.member.domain.Member;
import jparest.practice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member user = userRepository.findFirstUserByLoginIdOrderByIdAsc(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다"));

        return new UserDetailsImpl(
                user.getLoginId(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

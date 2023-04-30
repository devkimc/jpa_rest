package jparest.practice.auth.security;

import jparest.practice.auth.user.domain.User;
import jparest.practice.auth.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findFirstUserByLoginIdOrderByIdAsc(username).orElseThrow(() -> new RuntimeException("Not Found User"));
        return new UserDetailsImpl(
                user.getLoginId(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

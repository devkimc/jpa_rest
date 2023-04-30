package jparest.practice.auth.user.application;

import jparest.practice.auth.security.dto.LoginDTO;
import jparest.practice.auth.user.application.dto.UserInfoDTO;
import jparest.practice.auth.user.application.dto.UserJoinDTO;
import jparest.practice.auth.user.domain.User;
import jparest.practice.auth.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void join(UserJoinDTO userJoinDTO) {
        User alreadyUser = userRepository.findFirstUserByLoginIdOrderByIdAsc(userJoinDTO.getLoginId()).orElse(null);
        if (alreadyUser != null) throw new RuntimeException("이미 등록된 아이디입니다.");

        User saveUser = User.builder()
                .loginId(userJoinDTO.getLoginId())
                .password(passwordEncoder.encode(userJoinDTO.getPassword()))
                .email(userJoinDTO.getEmail())
                .name(userJoinDTO.getName())
                .build();

        userRepository.save(saveUser);
    }

    @Transactional
    public void login(LoginDTO loginDTO) {
        User member = userRepository.findByLoginId(loginDTO.getLoginId()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
    }

    @Transactional(readOnly = true)
    public UserInfoDTO getInfo(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
        return UserInfoDTO.builder()
                .id(user.getId())
                .userId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

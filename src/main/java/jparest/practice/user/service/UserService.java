package jparest.practice.user.service;

import jparest.practice.auth.config.UserType;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.user.dto.UserLoginRequest;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UserInfoResponse;
import jparest.practice.user.dto.UserLoginResponse;
import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Transactional
    public User join(User user) {
        User alreadyUser = userRepository.findFirstUserByLoginIdOrderByIdAsc(user.getLoginId()).orElse(null);
        if (alreadyUser != null) throw new ExistLoginIdException(alreadyUser.getLoginId());

        User saveUser = User.builder()
                .loginId(user.getLoginId())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .name(user.getName())
                .build();

        userRepository.save(saveUser);

        return saveUser;
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByLoginId(userLoginRequest.getLoginId()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(jwtService.createAccessToken(userLoginRequest.getLoginId(), UserType.ROLE_USER.name()));
        tokenDto.setRefreshToken(jwtService.createRefreshToken(userLoginRequest.getLoginId()));
        return new UserLoginResponse(user.getEmail(), user.getName(), tokenDto);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getInfo(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
        return UserInfoResponse.builder()
                .id(user.getId())
                .userId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

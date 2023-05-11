package jparest.practice.user.service;

import jparest.practice.auth.jwt.JwtService;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UserLoginResponse;
import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.exception.LoginFailException;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jparest.practice.user.domain.UserType;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
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
                .userType(UserType.ROLE_GENERAL)
                .build();

        userRepository.save(saveUser);

        return saveUser;
    }

    @Transactional
    public UserLoginResponse login(String loginId, String password) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new LoginFailException("존재하지 않는 유저 loginID = " + loginId));
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginFailException("잘못된 비밀번호 loginID = " + loginId);
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(jwtService.createAccessToken(loginId, UserType.ROLE_GENERAL.name()));
        tokenDto.setRefreshToken(jwtService.createRefreshToken(loginId));
        return new UserLoginResponse(user.getEmail(), user.getName(), tokenDto);
    }

//    @Transactional(readOnly = true)
//    public UserInfoResponse getInfo(long id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저 id = " + id));
//        return UserInfoResponse.builder()
//                .id(user.getId())
//                .userId(user.getLoginId())
//                .email(user.getEmail())
//                .name(user.getName())
//                .build();
//    }
}

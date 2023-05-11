package jparest.practice.user.service;

import jparest.practice.auth.jwt.JwtService;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.KakaoUserInfoDto;
import jparest.practice.user.dto.KakaoUserInfoResponse;
import jparest.practice.user.dto.UserLoginResponse;
import jparest.practice.user.exception.ExistLoginIdException;
import jparest.practice.user.exception.LoginFailException;
import jparest.practice.user.feign.KakaoFeignClient;
import jparest.practice.user.repository.UserRepository;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jparest.practice.user.domain.UserType;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private static final String BEARER = "Bearer ";
    private static final String grantType = "authorization_code";

    private final KakaoFeignClient kakaoFeignClient;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    @Value("${app.kakao.client-id}")
    private String clientId;

    @Value("${app.kakao.redirect-uri}")
    private String redirectUri;

    @Override
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

    @Override
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

    @Override
    @Transactional
    public String kakaoLogin(String code) {
        Map<String, Object> token = getKakaoToken(grantType, clientId, code, redirectUri);

        log.info("KAKAO TOKEN {}", token);

        String accessToken = BEARER + token.get("access_token");

        ResponseEntity<KakaoUserInfoDto> kakaoUserInfo = getKakaoUserInfo(accessToken);

        KakaoUserInfoDto userInfo = kakaoUserInfo.getBody();

        log.info("KAKAO USER INFO {}", kakaoUserInfo);

//        Long socialUserId = userInfo.getId();
//        Optional<User> findSocialUser = userRepository.findBySocialUserId(socialUserId);

        // TODO: 소셜로그인 이후 회원가입은 구현되지 않음
        return "pass";
    }

    private Map<String, Object> getKakaoToken(String grantType, String clientId, String code, String redirectUri) {
        try {
            return kakaoFeignClient.createToken(grantType, clientId, code, redirectUri);
        } catch (Exception e) {
            log.error("KAKAO CREATE TOKEN ERROR - {} ", e.getMessage());
            throw new LoginFailException("카카오 로그인 실패 code = " + code);
        }
    }

    private ResponseEntity<KakaoUserInfoDto> getKakaoUserInfo(String token) {
        try {
            ResponseEntity<KakaoUserInfoDto> userInfo = kakaoFeignClient.getUserInfo(new URI("https://kapi.kakao.com/v2/user/me"), token);
            System.out.println("userInfo = " + userInfo);
            return userInfo;
        } catch (Exception e) {
            log.error("KAKAO USER INFO ERROR - {} ", e.getMessage());
            throw new LoginFailException("카카오 정보 조회 실패 token = " + token);
        }
    }

}

package jparest.practice.member.service;

import jparest.practice.auth.config.UserType;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.member.dto.UserLoginRequest;
import jparest.practice.member.domain.Member;
import jparest.practice.member.dto.UserInfoResponse;
import jparest.practice.member.dto.UserJoinRequest;
import jparest.practice.member.dto.UserLoginResponse;
import jparest.practice.member.repository.ExistLoginIdException;
import jparest.practice.member.repository.MemberRepository;
import jparest.practice.common.util.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private static final String BEARER = "Bearer ";

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository userRepository;

    private final JwtService jwtService;

    @Transactional
    public void join(UserJoinRequest userJoinRequest) {
        Member alreadyUser = userRepository.findFirstUserByLoginIdOrderByIdAsc(userJoinRequest.getLoginId()).orElse(null);
        if (alreadyUser != null) throw new ExistLoginIdException(alreadyUser.getLoginId());

        Member saveUser = Member.builder()
                .loginId(userJoinRequest.getLoginId())
                .password(passwordEncoder.encode(userJoinRequest.getPassword()))
                .email(userJoinRequest.getEmail())
                .name(userJoinRequest.getName())
                .build();

        userRepository.save(saveUser);
    }

    @Transactional
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        Member member = userRepository.findByLoginId(userLoginRequest.getLoginId()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(userLoginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(jwtService.createAccessToken(userLoginRequest.getLoginId(), UserType.ROLE_USER.name()));
        tokenDto.setRefreshToken(jwtService.createRefreshToken(userLoginRequest.getLoginId()));
        return new UserLoginResponse(member.getEmail(), member.getName(), tokenDto);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getInfo(long id) {
        Member user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
        return UserInfoResponse.builder()
                .id(user.getId())
                .userId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

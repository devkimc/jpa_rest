package jparest.practice.member.service;

import jparest.practice.auth.config.UserType;
import jparest.practice.auth.jwt.JwtService;
import jparest.practice.member.dto.LoginDTO;
import jparest.practice.member.domain.Member;
import jparest.practice.member.dto.MemberInfoDTO;
import jparest.practice.member.dto.MemberJoinDTO;
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
    public void join(MemberJoinDTO memberJoinDTO) {
        Member alreadyUser = userRepository.findFirstUserByLoginIdOrderByIdAsc(memberJoinDTO.getLoginId()).orElse(null);
        if (alreadyUser != null) throw new RuntimeException("이미 등록된 아이디입니다.");

        Member saveUser = Member.builder()
                .loginId(memberJoinDTO.getLoginId())
                .password(passwordEncoder.encode(memberJoinDTO.getPassword()))
                .email(memberJoinDTO.getEmail())
                .name(memberJoinDTO.getName())
                .build();

        userRepository.save(saveUser);
    }

    @Transactional
    public TokenDto login(LoginDTO loginDTO) {
        Member member = userRepository.findByLoginId(loginDTO.getLoginId()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));
        if(!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(jwtService.createAccessToken(loginDTO.getLoginId(), UserType.ROLE_USER.name()));
        tokenDto.setRefreshToken(jwtService.createRefreshToken(loginDTO.getLoginId()));
        return tokenDto;
    }

    @Transactional(readOnly = true)
    public MemberInfoDTO getInfo(long id) {
        Member user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));
        return MemberInfoDTO.builder()
                .id(user.getId())
                .userId(user.getLoginId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}

//package jparest.practice.auth.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//@Log4j2
//public class AuthenticationProviderImpl implements AuthenticationProvider {
//
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    /**
//     *  인증 구현
//     */
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 전달 받은 UsernamePasswordAuthenticationToken
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
//
//        // AuthenticationFilter 에서 생성된 토큰으로부터 아이디와 비밀번호를 추출
//        String username = token.getName();
//        String password = (String) token.getCredentials();
//
//        //해당 회원 Database 조회
//        UserDetailsImpl userDetail = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
//
//        // 비밀번호 확인
//        if (!passwordEncoder.matches(password, userDetail.getPassword())) {
//            throw new BadCredentialsException(userDetail.getUsername() + "Invalid password");
//        }
//
//        // 인증 성공 시 UsernamePasswordAuthenticationToken 반환
//        return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), "", userDetail.getAuthorities());
//    }
//
//    /**
//     * provider 의 동작 여부를 설정
//     */
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}

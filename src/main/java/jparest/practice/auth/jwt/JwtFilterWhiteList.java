package jparest.practice.auth.jwt;

public class JwtFilterWhiteList {

    public static final String[] GET_WHITELIST = new String[]{
            "/docs/index.html",
    };

    public static final String[] POST_WHITELIST = new String[]{
            "/api/auth/login",
            "/api/auth/join",
            "/api/auth/kakao",
    };
}

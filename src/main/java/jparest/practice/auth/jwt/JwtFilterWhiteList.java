package jparest.practice.auth.jwt;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Jwt 를 검사하지 않는 api 를 모두 등록한다.
public class JwtFilterWhiteList {

    public static final String[] GET_WHITELIST = new String[]{
            "/docs/index.html",
            "/api/restaurants/ranking/most/save",
            "/api/restaurants/ranking/new/save",
    };

    public static final String[] POST_WHITELIST = new String[]{
            "/api/auth/login",
            "/api/auth/join",
            "/api/auth/kakao",
    };

    private static final List<String> getWhiteList = Arrays.asList(GET_WHITELIST);
    private static final List<String> postWhiteList = Arrays.asList(POST_WHITELIST);

    public static final List<String> WHITELIST = Stream.concat(
                    getWhiteList.stream(), postWhiteList.stream())
            .collect(Collectors.toList());
}

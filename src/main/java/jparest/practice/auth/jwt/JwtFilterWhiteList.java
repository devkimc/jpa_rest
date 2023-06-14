package jparest.practice.auth.jwt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JwtFilterWhiteList {

    public static final String[] GET_WHITELIST = new String[]{
            "/docs/index.html",
            "/api/restaurant/ranking/most/save",
            "/api/restaurant/ranking/new/save",
    };

    public static final String[] POST_WHITELIST = new String[]{
            "/api/auth/login",
            "/api/auth/join",
            "/api/auth/kakao",
    };

    private static final List<String> getWhiteList = Arrays.asList(GET_WHITELIST);
    private static final List<String> postWhiteList = Arrays.asList(POST_WHITELIST);

    private static final List<String> WHITELIST = new ArrayList<>();

    public static final List<String> getWhitelist() {
        WHITELIST.addAll(getWhiteList);
        WHITELIST.addAll(postWhiteList);

        return WHITELIST;
    }
}

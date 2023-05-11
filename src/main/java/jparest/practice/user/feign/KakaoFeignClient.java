package jparest.practice.user.feign;

import jparest.practice.user.config.KakaoFeignConfig;
import jparest.practice.user.dto.KakaoLoginResponse;
import jparest.practice.user.dto.KakaoUserInfoDto;
import jparest.practice.user.dto.KakaoUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Map;

@FeignClient(name = "kakao", url = "https://kauth.kakao.com", configuration = KakaoFeignConfig.class)
public interface KakaoFeignClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    Map<String, Object> createToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri
    );

    @GetMapping(consumes = {"application/x-www-form-urlencoded;charset=utf-8"})
    ResponseEntity<KakaoUserInfoDto> getUserInfo(
            URI uri,
            @RequestHeader("Authorization") String accessToken);
}

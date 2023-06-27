package jparest.practice.user.controller;

import jparest.practice.auth.security.CurrentUser;
import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UpdateUserInfoRequest;
import jparest.practice.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PatchMapping("/info")
    ApiResult<Boolean> updateNickname(@CurrentUser User user,
                                      @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return ApiUtils.success(userInfoService.updateUserInfo(user, updateUserInfoRequest));
    }
}

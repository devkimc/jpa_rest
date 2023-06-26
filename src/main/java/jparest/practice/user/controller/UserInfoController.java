package jparest.practice.user.controller;

import jparest.practice.common.util.ApiResult;
import jparest.practice.common.util.ApiUtils;
import jparest.practice.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping("/nickname/duplicate")
    ApiResult<Boolean> chkNickNameDuplicate(@RequestParam String nickname) {
        return ApiUtils.success(userInfoService.chkNickNameDuplicate(nickname));
    }

}

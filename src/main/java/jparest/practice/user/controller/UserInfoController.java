package jparest.practice.user.controller;

import jparest.practice.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

}

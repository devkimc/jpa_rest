package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.dto.UpdateUserInfoRequest;

public interface UserInfoService {

    // 유저 정보 변경
    Boolean updateUserInfo(User user, UpdateUserInfoRequest updateUserInfoRequest);
}

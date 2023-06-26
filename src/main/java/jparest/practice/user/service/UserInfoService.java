package jparest.practice.user.service;

import jparest.practice.user.domain.User;

public interface UserInfoService {

    // 닉네임 변경
    Boolean updateNickname(User user, String nickname);
}

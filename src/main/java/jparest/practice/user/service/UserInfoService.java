package jparest.practice.user.service;

public interface UserInfoService {

    // 닉네임 중복 검사
    Boolean chkNickNameDuplicate(String nickName);
}

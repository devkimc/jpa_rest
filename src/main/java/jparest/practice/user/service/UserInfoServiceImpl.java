package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService{

    private final UserRepository userRepository;

    // 닉네임 중복 시 true 반환
    @Override
    @Transactional(readOnly = true)
    public Boolean chkNickNameDuplicate(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);
        return optionalUser.isPresent();
    }
}

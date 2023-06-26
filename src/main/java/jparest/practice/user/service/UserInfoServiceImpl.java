package jparest.practice.user.service;

import jparest.practice.user.domain.User;
import jparest.practice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Boolean updateNickname(User user, String nickname) {
        user.setNickname(nickname);
        User saveUser = userRepository.save(user);

        System.out.println("saveUser = " + saveUser);
        return true;
    }
}

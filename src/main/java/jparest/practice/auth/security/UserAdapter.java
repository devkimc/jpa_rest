package jparest.practice.auth.security;

import jparest.practice.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class UserAdapter extends org.springframework.security.core.userdetails.User {
    private final User user;

    public UserAdapter(User user) {
        super(String.valueOf(user.getId()),
                "",
                List.of(new SimpleGrantedAuthority(user.getUserType().name())));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

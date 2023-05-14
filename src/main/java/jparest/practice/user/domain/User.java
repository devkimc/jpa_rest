package jparest.practice.user.domain;

import jparest.practice.group.domain.GroupUser;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 20)
    private String socialUserId;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String nickname;

    @Enumerated
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<GroupUser> groupUsers = new ArrayList<>();

// 아이디, 비밀번호 로그인 시
//    @Column(length = 20)
//    private String loginId;

//    @Column(length = 50)
//    private String password;
}

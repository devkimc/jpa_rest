package jparest.practice.user.domain;

import jparest.practice.group.domain.UserGroup;
import jparest.practice.invite.domain.Invite;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "varchar(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(length = 20)
    private String socialUserId;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "recvUser")
    private List<Invite> invites = new ArrayList<>();

// 아이디, 비밀번호 로그인 시
//    @Column(length = 20)
//    private String loginId;

//    @Column(length = 50)
//    private String password;
}

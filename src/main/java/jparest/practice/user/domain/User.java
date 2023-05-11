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
    private String loginId;

    @Column(length = 20)
    private String socialUserId;

    @Column(length = 50)
    private String password;

    @Column(length = 200)
    private String email;

    @Column(length = 50)
    private String name;

    @Column(length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<GroupUser> groupUsers = new ArrayList<>();
}

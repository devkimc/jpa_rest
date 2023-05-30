package jparest.practice.user.domain;

import jparest.practice.group.domain.UserGroup;
import jparest.practice.invite.domain.Invite;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
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

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups = new ArrayList<UserGroup>();

    @Builder.Default
    @OneToMany(mappedBy = "recvUser")
    private List<Invite> invites = new ArrayList<Invite>();

    //==연관관계 메서드==//
    public void addInvite(Invite invite) {
        invites.add(invite);
        invite.setRecvUser(this);
    }

    public boolean isJoinGroup(Long groupId) {
        long matchGroupCount = this.getUserGroups()
                .stream()
                .filter(e -> e.getGroup().getId().equals(groupId))
                .count();

        if (matchGroupCount == 1) {
            return true;
        }

        return false;
    }


// 아이디, 비밀번호 로그인 시
//    @Column(length = 20)
//    private String loginId;

//    @Column(length = 50)
//    private String password;
}

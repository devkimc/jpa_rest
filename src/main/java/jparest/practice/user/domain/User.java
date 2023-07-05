package jparest.practice.user.domain;

import jparest.practice.common.util.TimeBaseEntity;
import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.invite.domain.Invite;
import jparest.practice.subscription.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

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
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(length = 20)
    private String socialUserId;

    @Column(length = 40)
    private String email;

    @Column(length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<GroupUser> groupUsers = new ArrayList<GroupUser>();

    @Builder.Default
    @OneToMany(mappedBy = "recvUser")
    private List<Invite> invites = new ArrayList<Invite>();

    @Builder.Default
    @OneToMany(mappedBy = "applicant")
    private List<Subscription> subscriptions = new ArrayList<Subscription>();

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isJoinGroup(Long groupId) {
        long matchGroupCount = this.getGroupUsers()
                .stream()
                .filter(e -> e.getGroup().getId().equals(groupId))
                .count();

        return chkJoinGroupCount(matchGroupCount);
    }

    public boolean isJoinGroup(Group group) {
        long matchGroupCount = this.getGroupUsers()
                .stream()
                .filter(e -> e.getGroup().equals(group))
                .count();

        return chkJoinGroupCount(matchGroupCount);
    }

    private boolean chkJoinGroupCount(long count) {
        if (count == 1) {
            return true;
        }

        if (count > 1) {
            log.error(this.id + " 유저가 그룹된 가입은 총 " + count + "개 입니다.");
            return true;
        }

        return false;
    }

}

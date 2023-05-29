package jparest.practice.group.domain;

import jparest.practice.invite.domain.Invite;
import jparest.practice.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "user_group")
@Getter
@AllArgsConstructor
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id")
    private Group group;

    public void setGroup(Group group) {
        this.group = group;
    }

    public UserGroup(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    //==연관관계 메서드==//
    //OneToMany 필드에 주입시켜 준다.
    public void addUserGroup() {
        this.user.getUserGroups().add(this);
        this.group.getUserGroups().add(this);
    }

    //==생성 메서드==//
//    public static UserGroup createGroupUser(Optional<User> user) {
//        UserGroup userGroup = new UserGroup();
//        groupUser.setUser(user);
//        return userGroup;
//    }

}

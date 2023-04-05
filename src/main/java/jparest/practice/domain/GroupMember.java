package jparest.practice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_member")
@Getter
@Setter
public class GroupMember {

    @Id
    @GeneratedValue
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "groupMember")
    private List<Invite> invites = new ArrayList<>();

    //==생성 메서드==/
    public static GroupMember createGroupMember(Member member) {
        GroupMember groupMember = new GroupMember();
        groupMember.setMember(member);

        return groupMember;
    }

}

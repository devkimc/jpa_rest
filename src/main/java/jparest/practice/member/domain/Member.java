package jparest.practice.member.domain;

import jparest.practice.group.domain.GroupMember;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "member")
    private List<GroupMember> groupMembers = new ArrayList<>();
}

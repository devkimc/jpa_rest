package jparest.practice.user.domain;

import jparest.practice.group.domain.GroupMember;
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

    @Column(length = 100, nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 200)
    private String email;

    @Column(length = 50)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<GroupMember> groupMembers = new ArrayList<>();
}

package jparest.practice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Invite {

    @Id
    @GeneratedValue
    @Column(name = "invite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String groupName;
    private Long recvUserId;

    @Enumerated(EnumType.STRING)
    private InviteStatus status;

}

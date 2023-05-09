package jparest.practice.group.service;

import jparest.practice.group.domain.Group;
import jparest.practice.group.domain.GroupUser;
import jparest.practice.group.repository.GroupRepository;
import jparest.practice.user.domain.User;
import jparest.practice.user.repository.MemberRepositoryV0;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final MemberRepositoryV0 memberRepository;
    private final GroupRepository groupRepository;

    /**
     * 그룹 생성
     */
    @Transactional
    public Long makeGroup(Long memberId, String groupName) {

        // 엔티티 조회
        User user = memberRepository.findOne(memberId);

        // 그룹 멤버 생성
        GroupUser groupUser = GroupUser.createGroupUser(user);

        // 그룹 생성
        Group group = Group.createGroup(memberId, groupName, groupUser);

        // 그룹 저장
        groupRepository.save(group);
        return group.getId();
    }
}

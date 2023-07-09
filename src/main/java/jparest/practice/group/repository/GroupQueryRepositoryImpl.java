package jparest.practice.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jparest.practice.group.domain.GroupUserType;
import jparest.practice.group.dto.QSearchGroupListResponse;
import jparest.practice.group.dto.SearchGroupListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jparest.practice.group.domain.QGroup.group;
import static jparest.practice.group.domain.QGroupUser.groupUser;
import static jparest.practice.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepositoryImpl implements GroupQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<SearchGroupListResponse> search(String groupName, String ownerNickname) {
        return query.select(new QSearchGroupListResponse(
                                group.id,
                                group.groupName,
                                user.nickname,
                                group.updatedAt
                ))
                .from(group)
                .join(group.groupUsers, groupUser)
                .join(groupUser.user, user)
                .where(group.groupName.contains(groupName),
                        groupUser.user.nickname.contains(ownerNickname),
                        groupUser.groupUserType.eq(GroupUserType.ROLE_OWNER),
                        group.isPublic.isTrue()
                )
                .fetch();
    }
}

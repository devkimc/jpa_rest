package jparest.practice.group.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jparest.practice.common.util.QueryUtils;
import jparest.practice.group.domain.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static jparest.practice.group.domain.QGroup.group;
import static jparest.practice.group.domain.QGroupUser.groupUser;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepositoryImpl implements GroupQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<List<Group>> search(String groupName, String ownerNickname) {
        return Optional.ofNullable(
                query.selectFrom(group).distinct()
                        .innerJoin(group.groupUsers, groupUser).fetchJoin()
                        .where(QueryUtils.parseLikeString(group.groupName, groupName),
                                QueryUtils.parseLikeString(groupUser.user.nickname, ownerNickname))
                        .fetch()
        );
    }
}

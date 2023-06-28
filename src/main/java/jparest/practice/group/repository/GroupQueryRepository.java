package jparest.practice.group.repository;

import jparest.practice.group.domain.Group;

import java.util.List;
import java.util.Optional;

public interface GroupQueryRepository {

    Optional<List<Group>> search(String groupName, String ownerNickname);
}

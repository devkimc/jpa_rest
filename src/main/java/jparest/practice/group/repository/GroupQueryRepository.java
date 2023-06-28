package jparest.practice.group.repository;

import jparest.practice.group.dto.SearchGroupListResponse;

import java.util.List;

public interface GroupQueryRepository {

    List<SearchGroupListResponse> search(String groupName, String ownerNickname);
}

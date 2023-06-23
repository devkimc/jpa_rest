package jparest.practice.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class GetGroupUserResponse {
    private Long groupId;
    private String groupName;
    private int totalUsers;
//    private int totalFavRest;
}

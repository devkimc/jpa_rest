package jparest.practice.group.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserGroupResponse {
    private Long groupId;
    private String groupName;
    private int totalUsers;
//    private int totalFavRest;
}

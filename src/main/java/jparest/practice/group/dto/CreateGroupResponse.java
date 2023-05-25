package jparest.practice.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateGroupResponse {
    private Long id;
    private String groupName;
}

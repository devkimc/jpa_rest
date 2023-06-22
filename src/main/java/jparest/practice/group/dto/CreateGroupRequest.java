package jparest.practice.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGroupRequest {

    @Size(min = 2, max = 20, message = "그룹명은 2~20 글자여야 합니다.")
    private String groupName;

    private Boolean isPublic;
}

package jparest.practice.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchGroupListResponse {

    private Long groupId;

    private String groupName;

    private String ownerNickname;

    private int totalGroupUser;

    private LocalDateTime updatedAt;
}

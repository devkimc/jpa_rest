package jparest.practice.group.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class SearchGroupListResponse {

    private Long groupId;

    private String groupName;

    private String ownerNickname;

//    private int totalGroupUser;

    private LocalDateTime updatedAt;

    @QueryProjection
    public SearchGroupListResponse(Long groupId, String groupName, String ownerNickname, LocalDateTime updatedAt) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.ownerNickname = ownerNickname;
        this.updatedAt = updatedAt;
    }
}

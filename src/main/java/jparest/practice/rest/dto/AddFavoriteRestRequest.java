package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFavoriteRestRequest {
    private Long groupId;
    private String restName;
    private double latitude;
    private double longitude;
}

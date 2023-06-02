package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddFavoriteRestRequest {
    private Long groupId;
    private String restName;
    private double latitude;
    private double longitude;
}

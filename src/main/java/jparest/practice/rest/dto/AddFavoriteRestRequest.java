package jparest.practice.rest.dto;

import lombok.Getter;

@Getter
public class AddFavoriteRestRequest {
    private Long groupId;
    private String restName;
    private double latitude;
    private double longitude;
}

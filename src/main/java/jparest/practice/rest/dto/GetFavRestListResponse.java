package jparest.practice.rest.dto;

import lombok.Getter;

@Getter
public class GetFavRestListResponse {
    private String restId;
    private String groupName;
    private double latitude;
    private double longitude;
}

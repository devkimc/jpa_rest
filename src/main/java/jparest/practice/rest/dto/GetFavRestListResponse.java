package jparest.practice.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFavRestListResponse {
    private String restId;
    private String restName;
    private double latitude;
    private double longitude;
}

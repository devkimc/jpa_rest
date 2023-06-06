package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetFavRestListResponse {
    private String restId;
    private String restName;
    private double latitude;
    private double longitude;
}

package jparest.practice.rest.dto;

import lombok.Getter;

@Getter
public class AddFavoriteRestRequest {
    private String restName;
    private double latCdnt;
    private double lngCdnt;
}

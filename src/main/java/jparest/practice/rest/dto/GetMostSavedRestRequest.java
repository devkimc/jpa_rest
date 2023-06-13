package jparest.practice.rest.dto;

import lombok.Getter;

@Getter
public class GetMostSavedRestRequest {
    private int rank;
    private Long restId;
    private String restName;
    private int totalFavorite;
}

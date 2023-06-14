package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetMostSavedRestResponse {
    private int rank;
    private String restId;
    private String restName;
    private int totalFavorite;
}

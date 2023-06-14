package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class GetNewSavedRestResponse {
    private int rank;
    private String restId;
    private String restName;
    private LocalDateTime savedAt;
}

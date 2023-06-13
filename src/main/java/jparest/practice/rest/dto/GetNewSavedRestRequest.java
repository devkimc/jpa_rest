package jparest.practice.rest.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetNewSavedRestRequest {
    private int rank;
    private Long restId;
    private String restName;
    private LocalDateTime savedAt;
}

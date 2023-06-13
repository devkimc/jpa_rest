package jparest.practice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFavoriteRestRequest {
    @Min(1)
    private Long groupId;

    @NotBlank
    private String restName;

    @NotNull
    private double latitude;

    @NotNull
    private double longitude;
}

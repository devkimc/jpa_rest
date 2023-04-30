package jparest.practice.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DataApiResponse<T> extends ApiResponse {
    private T result;
}

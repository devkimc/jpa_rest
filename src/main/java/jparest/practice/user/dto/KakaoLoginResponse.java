package jparest.practice.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class KakaoLoginResponse {

    private Long id;

    public KakaoLoginResponse(Long id) {
        this.id = id;
    }
}

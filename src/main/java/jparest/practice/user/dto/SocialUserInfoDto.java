package jparest.practice.user.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialUserInfoDto {

    private Long id;
    private String connected_at;
    private KakaoAccount kakao_account;

    public SocialUserInfoDto(Long id, String connectedAt, KakaoAccount kakaoAccount) {
        this.id = id;
        this.connected_at = connectedAt;
        this.kakao_account = kakaoAccount;
    }

    @Getter
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private ProFile profile;
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private boolean is_email_verified;
        private String email;
        private String phone_number;
        private boolean phone_number_needs_agreement;

        public KakaoAccount(
                boolean profile_nickname_needs_agreement,
                ProFile profile,
                boolean has_email,
                boolean email_needs_agreement,
                boolean is_email_valid,
                boolean is_email_verified,
                String email,
                String phone_number,
                boolean phone_number_needs_agreement
        ) {
            this.profile_nickname_needs_agreement = profile_nickname_needs_agreement;
            this.profile = profile;
            this.has_email = has_email;
            this.email_needs_agreement = email_needs_agreement;
            this.is_email_valid = is_email_valid;
            this.is_email_verified = is_email_verified;
            this.email = email;
            this.phone_number = phone_number;
            this.phone_number_needs_agreement = phone_number_needs_agreement;

        }

        @Getter
        @NoArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ProFile {
            private String nickname;

            public ProFile(String nickname) {
                this.nickname = nickname;
            }
        }
    }
}

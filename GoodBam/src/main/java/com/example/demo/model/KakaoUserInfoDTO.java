package com.example.demo.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// 카카오에서 토큰을 통해 반환되는 사용자 정보를 저장할 DTO
public class KakaoUserInfoDTO {
	private KakaoAccount kakao_account;
    private Properties properties;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
    }

    @Getter
    @Setter
    public static class Properties {
        private String nickname;
    }
}

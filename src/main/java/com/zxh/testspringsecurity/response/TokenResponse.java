package com.zxh.testspringsecurity.response;

import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class TokenResponse {

    private String accessToken;

}

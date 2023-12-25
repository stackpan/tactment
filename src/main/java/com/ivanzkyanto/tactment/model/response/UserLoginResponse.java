package com.ivanzkyanto.tactment.model.response;

import com.ivanzkyanto.tactment.util.Token;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {

    private String token;

    private Long expiredAt;

    public static UserLoginResponse build(Token token) {
        return new UserLoginResponse(token.getToken(), token.getExpiredAt());
    }

}

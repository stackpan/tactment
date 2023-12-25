package com.ivanzkyanto.tactment.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Token {

    private String token;

    private Long expiredAt;

    public static Token generate() {
        return generate(3600L);
    }

    public static Token generate(Long expirationInSecs) {
        return new Token(UUID.randomUUID().toString(), System.currentTimeMillis() + (1000 * expirationInSecs));
    }

}

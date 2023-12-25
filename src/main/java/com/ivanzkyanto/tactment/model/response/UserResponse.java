package com.ivanzkyanto.tactment.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String username;

    private String name;

}

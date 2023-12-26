package com.ivanzkyanto.tactment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest implements Request {

    @NotBlank
    @Size(max = 100)
    private String oldPassword;

    @NotBlank
    @Size(max = 100)
    private String newPassword;

    @NotBlank
    @Size(max = 100)
    private String newPasswordConfirmation;

}

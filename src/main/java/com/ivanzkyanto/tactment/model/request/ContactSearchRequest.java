package com.ivanzkyanto.tactment.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactSearchRequest implements Request {

    @Size(max = 100)
    private String name;

    @Size(max = 100)
    private String phone;

    @Size(max = 100)
    private String email;

    @NonNull
    @Min(0)
    private Integer page;

    @NonNull
    private Integer size;

}

package com.ivanzkyanto.tactment.model.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressUpdateRequest implements Request {

    @Size(max = 200)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @NonNull
    @Size(max = 100)
    private String country;

    @Size(max = 10)
    private String postalCode;

}

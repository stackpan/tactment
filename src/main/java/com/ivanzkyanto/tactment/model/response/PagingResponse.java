package com.ivanzkyanto.tactment.model.response;

import com.ivanzkyanto.tactment.model.PagingData;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse<T> {

    private T data;

    private PagingData paging;

}

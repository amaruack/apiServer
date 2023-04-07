package com.son.api.dto.shop;

import com.son.api.dto.QueryParam;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShopHistoryQueryParam implements QueryParam {

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

}

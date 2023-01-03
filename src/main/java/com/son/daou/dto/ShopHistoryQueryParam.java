package com.son.daou.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShopHistoryQueryParam implements QueryParam {

    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;

}

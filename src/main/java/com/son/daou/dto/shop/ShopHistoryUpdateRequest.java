package com.son.daou.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.son.daou.domain.shop.ShopHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopHistoryUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH", timezone = "Asia/Seoul")
    LocalDateTime dateTime;
    Integer registerCount;
    Integer deleteCount;
    Integer payment;
    Integer used;
    Integer sales;

    public ShopHistory toEntity(){
        return ShopHistory.builder()
                .dateTime(dateTime)
                .registerCount(registerCount)
                .deleteCount(deleteCount)
                .payment(payment)
                .used(used)
                .sales(sales)
            .build();
    }
}

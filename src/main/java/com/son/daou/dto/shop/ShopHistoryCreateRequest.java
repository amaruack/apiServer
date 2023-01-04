package com.son.daou.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.son.daou.domain.shop.ShopHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopHistoryCreateRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH", timezone = "Asia/Seoul")
    LocalDateTime dateTime;

    @NotNull
    @PositiveOrZero
    Integer registerCount;

    @NotNull
    @PositiveOrZero
    Integer deleteCount;

    @NotNull
    Long payment;

    @NotNull
    Long used;

    @NotNull
    Long sales;

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

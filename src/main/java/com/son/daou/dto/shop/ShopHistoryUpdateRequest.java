package com.son.daou.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ShopHistoryUpdateRequest {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_TIME_ID_PATTERN, timezone = DateTimeUtils.ZONE_NAME)
    @Schema( hidden = true)
    String dateTime;

    @Schema( title = "등록자 수", example = "24")
    Integer registerCount;
    @Schema( title = "탈퇴자 수", example = "24")
    Integer deleteCount;
    @Schema( title = "결제금액", example = "10000")
    Long payment;
    @Schema( title = "사용금액", example = "10000")
    Long used;
    @Schema( title = "매출금액", example = "10000")
    Long sales;

    public ShopHistory toEntity(){
        return ShopHistory.builder()
                .dateTime(LocalDateTime.parse(dateTime, DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .registerCount(registerCount)
                .deleteCount(deleteCount)
                .payment(payment)
                .used(used)
                .sales(sales)
            .build();
    }
}

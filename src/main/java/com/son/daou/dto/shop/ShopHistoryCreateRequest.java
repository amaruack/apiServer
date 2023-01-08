package com.son.daou.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopHistoryCreateRequest {

    @NotNull
    @Pattern(regexp = DateTimeUtils.DATE_TIME_ID_REGEX)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_TIME_ID_PATTERN, timezone = DateTimeUtils.ZONE_NAME)
    @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, title = "date time", example = "2023-01-07T00")
    String dateTime;

    @NotNull
    @PositiveOrZero
    @Schema( title = "등록자 수", example = "24")
    Integer registerCount;

    @NotNull
    @PositiveOrZero
    @Schema( title = "탈퇴자 수", example = "24")
    Integer deleteCount;

    @NotNull
    @Schema( title = "결제금액", example = "10000")
    Long payment;

    @NotNull
    @Schema( title = "사용금액", example = "10000")
    Long used;

    @NotNull
    @Schema( title = "매출금액", example = "10000")
    Long sales;

    public ShopHistory toEntity(){
        return ShopHistory.builder()
                .dateTime(LocalDateTime.parse( dateTime, DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .registerCount(registerCount)
                .deleteCount(deleteCount)
                .payment(payment)
                .used(used)
                .sales(sales)
            .build();
    }
}

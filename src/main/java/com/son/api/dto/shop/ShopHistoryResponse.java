package com.son.api.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.son.api.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "content")
public class ShopHistoryResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_TIME_ID_PATTERN, timezone = DateTimeUtils.ZONE_NAME)
    @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, title = "date time", example = "2023-01-07T00")
    String dateTime;
    Integer registerCount;
    Integer deleteCount;
    Long payment;
    Long used;
    Long sales;

}

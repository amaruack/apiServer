package com.son.daou.dto.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "content")
public class ShopHistoryResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH", timezone = "Asia/Seoul")
    LocalDateTime dateTime;
    Integer registerCount;
    Integer deleteCount;
    Long payment;
    Long used;
    Long sales;

}

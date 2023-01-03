package com.son.daou.dto.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopHistoryResponse {

    LocalDateTime dateTime;
    Integer registerCount;
    Integer deleteCount;
    Integer payment;
    Integer used;
    Integer sales;

}

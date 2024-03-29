package com.son.api.domain.shop;

import com.son.api.domain.DaouEntity;
import com.son.api.dto.shop.ShopHistoryResponse;
import com.son.api.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shop_history")
public class ShopHistory implements DaouEntity {

    @Id
    @Column(name = "date_time")
    LocalDateTime dateTime;

    @Column(name = "register_count")
    Integer registerCount;

    @Column(name = "delete_count")
    Integer deleteCount;

    @Column(name = "payment")
    Long payment;

    @Column(name = "used")
    Long used;

    @Column(name = "sales")
    Long sales;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ShopHistoryResponse toResponse(){
        return ShopHistoryResponse.builder()
                .dateTime(dateTime.format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .registerCount(registerCount)
                .deleteCount(deleteCount)
                .payment(payment)
                .used(used)
                .sales(sales)
            .build();
    }

}

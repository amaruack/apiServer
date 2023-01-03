package com.son.daou.domain.shop;

import com.son.daou.domain.DaouEntity;
import com.son.daou.dto.shop.ShopHistoryResponse;
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
    Integer payment;

    @Column(name = "used")
    Integer used;

    @Column(name = "sales")
    Integer sales;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public ShopHistoryResponse toResponse(){
        return ShopHistoryResponse.builder()
                .dateTime(dateTime)
                .registerCount(registerCount)
                .deleteCount(deleteCount)
                .payment(payment)
                .used(used)
                .sales(sales)
            .build();
    }

}

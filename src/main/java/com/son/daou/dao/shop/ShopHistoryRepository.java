package com.son.daou.dao.shop;

import com.son.daou.dao.RepositorySearch;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, LocalDateTime>, RepositorySearch<ShopHistory, ShopHistoryQueryParam> {

    default ShopHistory update(ShopHistory updateRequest) {
        ShopHistory findData = findById(updateRequest.getDateTime()).get();

        if(updateRequest.getRegisterCount() != null) {
            findData.setRegisterCount(updateRequest.getRegisterCount());
        }

        if(updateRequest.getDeleteCount() != null) {
            findData.setDeleteCount(updateRequest.getDeleteCount());
        }

        if(updateRequest.getPayment() != null) {
            findData.setPayment(updateRequest.getPayment());
        }

        if(updateRequest.getUsed() != null) {
            findData.setUsed(updateRequest.getUsed());
        }

        if(updateRequest.getSales() != null) {
            findData.setSales(updateRequest.getSales());
        }

        saveAndFlush(findData);
        return findData;
    }


}

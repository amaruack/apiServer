package com.son.daou.dao;

import com.son.daou.domain.ShopHistory;
import com.son.daou.dto.ShopHistoryQueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, LocalDateTime>, RepositorySearch<ShopHistory, ShopHistoryQueryParam> {

    default ShopHistory update(ShopHistory updateRequest) {
        ShopHistory findData = findById(updateRequest.getDateTime()).orElseThrow(() -> {
            throw new RuntimeException("NOT FOUND"); // TODO 임시처리 나중에 통합 exception 처리
        });

        if(updateRequest.getRegisterCount() != null) {
            findData.setRegisterCount(updateRequest.getRegisterCount());
        }

        if(updateRequest.getRegisterCount() != null) {
            findData.setRegisterCount(updateRequest.getRegisterCount());
        }

        if(updateRequest.getRegisterCount() != null) {
            findData.setRegisterCount(updateRequest.getRegisterCount());
        }

        if(updateRequest.getRegisterCount() != null) {
            findData.setRegisterCount(updateRequest.getRegisterCount());
        }

        saveAndFlush(findData);
        return findData;
    }


}

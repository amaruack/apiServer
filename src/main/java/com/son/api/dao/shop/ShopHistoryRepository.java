package com.son.api.dao.shop;

import com.son.api.common.ErrorCode;
import com.son.api.common.exception.ApiException;
import com.son.api.dao.RepositorySearch;
import com.son.api.domain.shop.ShopHistory;
import com.son.api.dto.shop.ShopHistoryQueryParam;
import com.son.api.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShopHistoryRepository extends JpaRepository<ShopHistory, LocalDateTime>, RepositorySearch<ShopHistory, ShopHistoryQueryParam> {

    Logger log = LoggerFactory.getLogger(ShopHistoryRepository.class);

    default ShopHistory update(ShopHistory updateRequest) {

        ShopHistory findData = findById(updateRequest.getDateTime()).orElseThrow(() -> {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), updateRequest.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        });

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

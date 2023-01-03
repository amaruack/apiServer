package com.son.daou.service;

import com.son.daou.common.ErrorCode;
import com.son.daou.common.exception.ApiException;
import com.son.daou.dao.shop.ShopHistoryRepository;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class ShopHistoryService {

    private Logger logger = LoggerFactory.getLogger(ShopHistoryService.class);

    @Autowired
    private ShopHistoryRepository repository;

    public Page<ShopHistoryResponse> search(ShopHistoryQueryParam queryParam, Pageable pageable) {
        Page<ShopHistory> shopHistorys = repository.search(queryParam, pageable);
        return shopHistorys.map(ShopHistory::toResponse);
    }

    @Transactional
    public ShopHistoryResponse create(ShopHistoryCreateRequest createRequest) {
        // response data
        ShopHistoryResponse response = null;

        // ShopHistory entity builder
        ShopHistory shopHistory = createRequest.toEntity();

        // ShopHistory id 중복
        if (!repository.existsById(shopHistory.getDateTime())) {
            // ShopHistory save
            repository.save(shopHistory);
        } else {
            String detailErrorMessage = String.format(ErrorCode.CONFLICT.getDetailMessageFormat(), shopHistory.getDateTime());
            logger.error(detailErrorMessage);
            throw new ApiException(ErrorCode.CONFLICT, detailErrorMessage);
        }
        // response data builder
        response = shopHistory.toResponse();

        return response;
    }

    public ShopHistoryResponse findById(LocalDateTime id) throws ApiException {
        ShopHistory shopHistory = repository.findById(id).orElseThrow(() -> {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), id);
            logger.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        });
        return shopHistory.toResponse();
    }

    @Transactional
    public ShopHistoryResponse update(ShopHistoryUpdateRequest shopHistoryUpdateRequest) {
        return repository
                .update(shopHistoryUpdateRequest.toEntity())
                .toResponse();
    }

    @Transactional
    public ShopHistoryResponse delete(LocalDateTime id) {
        ShopHistory findData = repository.findById(id).orElseThrow(() -> {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), id);
            logger.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        });
        repository.delete(findData);
        return findData.toResponse();
    }

}

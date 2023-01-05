package com.son.daou.service.shop;

import com.son.daou.common.ErrorCode;
import com.son.daou.common.exception.ApiException;
import com.son.daou.dao.shop.ShopHistoryRepository;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import com.son.daou.util.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ShopHistoryService {

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
            String detailErrorMessage = String.format(ErrorCode.CONFLICT.getDetailMessageFormat(), shopHistory.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.CONFLICT, detailErrorMessage);
        }
        // response data builder
        response = shopHistory.toResponse();

        return response;
    }

    @Transactional
    public List<ShopHistoryResponse> createAll(List<ShopHistoryCreateRequest> createRequests) {
        // response data
        List<ShopHistoryResponse> response = null;
        List<ShopHistory> entities = createRequests.stream().map(request -> request.toEntity()).collect(Collectors.toList());

        List<LocalDateTime> ids = entities.stream().map(entity -> entity.getDateTime()).collect(Collectors.toList());
        List<ShopHistory> findDatas = repository.findAllById(ids);
        // ShopHistory id 중복
        if (findDatas.size() <= 0) {
            // ShopHistory save
            entities = repository.saveAll(entities);
        } else {
            String dupleIds = findDatas.stream().map(findData -> findData.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)).collect(Collectors.joining(","));
            String detailErrorMessage = String.format(ErrorCode.CONFLICT.getDetailMessageFormat(), dupleIds);
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.CONFLICT, detailErrorMessage);
        }
        // response data builder
        response = entities.stream().map(entity -> entity.toResponse()).collect(Collectors.toList());

        return response;
    }

    public ShopHistoryResponse findById(LocalDateTime id) throws ApiException {
        ShopHistory shopHistory = repository.findById(id).orElseThrow(() -> {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), id.format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        });
        return shopHistory.toResponse();
    }

    @Transactional
    public ShopHistoryResponse update(ShopHistoryUpdateRequest shopHistoryUpdateRequest) {
        if (!repository.existsById(shopHistoryUpdateRequest.getDateTime())) {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), shopHistoryUpdateRequest.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        };
        return repository
                .update(shopHistoryUpdateRequest.toEntity())
                .toResponse();
    }

    @Transactional
    public ShopHistoryResponse delete(LocalDateTime id) {

        ShopHistory findData = repository.findById(id).orElseThrow(() -> {
            String detailErrorMessage = String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), id.format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
            log.error(detailErrorMessage);
            throw new ApiException(ErrorCode.NOT_FOUND_DATA, detailErrorMessage);
        });
        repository.delete(findData);
        return findData.toResponse();
    }

}

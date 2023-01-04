package com.son.daou.slice.service;

import com.son.daou.common.ErrorCode;
import com.son.daou.common.exception.ApiException;
import com.son.daou.dao.shop.ShopHistoryRepository;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import com.son.daou.service.ShopHistoryService;
import com.son.daou.util.DateTimeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Import({ShopHistoryService.class})
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShopHistoryServiceTest {

    @Mock
    ShopHistoryRepository shopHistoryRepository;

    @InjectMocks
    ShopHistoryService situationEventService;

    ShopHistoryCreateRequest createRequest1;
    ShopHistoryCreateRequest createRequest2;
    ShopHistory history1 ;
    ShopHistory history2 ;
    ShopHistoryUpdateRequest updateRequest1;

    ShopHistoryQueryParam searchParam ;

    @BeforeAll
    public void beforeAll() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        createRequest1 = ShopHistoryCreateRequest.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000L)
                .used(20000L)
                .sales(300000L)
                .build();

        createRequest2 = ShopHistoryCreateRequest.builder()
                .dateTime(now.plus(1, ChronoUnit.HOURS))
                .registerCount(10)
                .deleteCount(20)
                .payment(10000L)
                .used(20000L)
                .sales(300000L)
                .build();
        
        updateRequest1 = ShopHistoryUpdateRequest.builder()
                .dateTime(createRequest1.getDateTime())
                .registerCount(232)
                .deleteCount(242)
                .build();

        history1 = createRequest1.toEntity();
        history2 = createRequest2.toEntity();

        searchParam = ShopHistoryQueryParam.builder()
                .build();
    }

    @Test
    void success_history_info_search() {

        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<ShopHistory> list = List.of(history1, history2);
        Page<ShopHistory> pages = new PageImpl(list, pageable, list.size());
        doReturn(pages).when(shopHistoryRepository).search(searchParam, pageable);

        // when
        Page<ShopHistoryResponse> returnValue = situationEventService.search(searchParam, pageable);

        // then
        assertEquals(pages.getTotalElements(), returnValue.getTotalElements());
        assertEquals(pages.stream().map(history -> history.getDateTime()).collect(Collectors.toList()),
                returnValue.stream().map(response -> response.getDateTime()).collect(Collectors.toList()));
    }

    @Test
    void success_history_info_create() {

        // given
        doReturn(false).when(shopHistoryRepository).existsById(history1.getDateTime());
        Mockito.doReturn(createRequest1.toEntity()).when(shopHistoryRepository).save(any());

        // when
        ShopHistoryResponse returnValue = situationEventService.create(createRequest1);

        // then
        assertEquals(history1.getDateTime(), returnValue.getDateTime());
    }

    @Test
    void fail_history_info_create_already_history_id() {
        // given
        doReturn(true).when(shopHistoryRepository).existsById(history1.getDateTime());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            ShopHistoryResponse returnValue = situationEventService.create(createRequest1);
        } );

        // then
        assertEquals(String.format(ErrorCode.CONFLICT.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)),
                exception.getMessage());
        assertEquals(ErrorCode.CONFLICT, exception.getErrorCode());
    }

    @Test
    void success_history_info_find_by_history_id() {

        // given
        doReturn(Optional.of(history1)).when(shopHistoryRepository).findById(history1.getDateTime());

        // when
        ShopHistoryResponse returnValue = situationEventService.findById(history1.getDateTime());

        // then
        assertEquals(history1.getDateTime(), returnValue.getDateTime());
        assertEquals(history1.getRegisterCount(), returnValue.getRegisterCount());
        assertEquals(history1.getDeleteCount(), returnValue.getDeleteCount());
        assertEquals(history1.getPayment(), returnValue.getPayment());
        assertEquals(history1.getUsed(), returnValue.getUsed());
        assertEquals(history1.getSales(), returnValue.getSales());
    }

    @Test
    void fail_history_info_find_not_exist_history_id() {

        // given
        doReturn(Optional.empty()).when(shopHistoryRepository).findById(history1.getDateTime());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            ShopHistoryResponse returnValue = situationEventService.findById(history1.getDateTime());
        } );

        // then
        assertEquals(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)),
                exception.getMessage());
        assertEquals(ErrorCode.NOT_FOUND_DATA, exception.getErrorCode());
    }

    @Test
    void success_history_info_update() {
        // given
        doReturn(history1).when(shopHistoryRepository).update(any());

        // when
        ShopHistoryResponse returnValue = situationEventService.update(updateRequest1);

        // then
        assertEquals(history1.getDateTime(), returnValue.getDateTime());
    }

    @Test
    void fail_history_info_update_not_exist_data_from_history_id() {
        // given
        doThrow(new ApiException(ErrorCode.NOT_FOUND_DATA, String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime())))
                .when(shopHistoryRepository).update(any());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            ShopHistoryResponse returnValue = situationEventService.update(updateRequest1);
        } );

        // then
        assertEquals(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime()), exception.getMessage());
    }

    @Test
    void success_history_info_delete() {
        // given
        doReturn(Optional.of(history1)).when(shopHistoryRepository).findById(history1.getDateTime());
        doNothing().when(shopHistoryRepository).delete(any());

        // when
        ShopHistoryResponse returnValue = situationEventService.delete(history1.getDateTime());

        // then
        assertEquals(history1.getDateTime(), returnValue.getDateTime());
    }

    @Test
    void fail_history_info_delete_not_exist_history_id() {
        // given
        doReturn(Optional.empty()).when(shopHistoryRepository).findById(history1.getDateTime());

        // when
        ApiException exception = assertThrows(ApiException.class, () -> {
            ShopHistoryResponse returnValue = situationEventService.delete(history1.getDateTime());
        } );

        // then
        assertEquals(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)), exception.getMessage());
    }

}

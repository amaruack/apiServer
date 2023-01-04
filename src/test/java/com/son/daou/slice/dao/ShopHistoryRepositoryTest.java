package com.son.daou.slice.dao;

import com.son.daou.common.ErrorCode;
import com.son.daou.config.QuerydslConfig;
import com.son.daou.dao.shop.ShopHistoryRepository;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.util.DateTimeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaSystemException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({QuerydslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShopHistoryRepositoryTest {

    @Autowired
    ShopHistoryRepository shopHistoryRepository;

    @Test
    void success_shop_history_save(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();

        //when
        shopHistoryRepository.save(shopHistory);

        //then
        List<ShopHistory> findAll = shopHistoryRepository.findAll();
        assertEquals(1, findAll.size());

    }

    @Test
    void fail_shop_history_save_id_is_null(){
        //given
        ShopHistory shopHistory = ShopHistory.builder()
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();

        //when // then
        assertThrows(JpaSystemException.class, () -> {
            shopHistoryRepository.save(shopHistory);
        });

    }

    @Test
    void success_shop_history_save_all(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<ShopHistory> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ShopHistory shopHistory = ShopHistory.builder()
                    .dateTime(now.plus(i, ChronoUnit.HOURS))
                    .registerCount(10)
                    .deleteCount(20)
                    .payment(10000)
                    .used(20000)
                    .sales(300000)
                    .build();
            list.add(shopHistory);
        }

        //when
        shopHistoryRepository.saveAll(list);

        //then
        List<ShopHistory> findAll = shopHistoryRepository.findAll();
        assertEquals(24, findAll.size());

    }

    @Test
    void success_shop_history_search(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        List<ShopHistory> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ShopHistory shopHistory = ShopHistory.builder()
                    .dateTime(now.plus(i, ChronoUnit.HOURS))
                    .registerCount(10)
                    .deleteCount(20)
                    .payment(10000)
                    .used(20000)
                    .sales(300000)
                    .build();
            list.add(shopHistory);
        }
        shopHistoryRepository.saveAll(list);

        ShopHistoryQueryParam queryParam = ShopHistoryQueryParam.builder()
                .startDatetime(now)
                .endDatetime(now.plus(4,ChronoUnit.HOURS))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 99);

        //when
        Page<ShopHistory> searchData =  shopHistoryRepository.search(queryParam, pageRequest);

        //then
        assertEquals(3, searchData.getTotalElements());

    }

    @Test
    void fail_success_shop_history_search_invalid_sort_index(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();
        shopHistoryRepository.save(shopHistory);

        String notInProperty = "aaa";
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.Direction.fromString("DESC"), notInProperty);
        ShopHistoryQueryParam queryParam = ShopHistoryQueryParam.builder()
                .startDatetime(now)
                .endDatetime(now.plus(4,ChronoUnit.HOURS))
                .build();

        //when
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            Page<ShopHistory> list = shopHistoryRepository.search(queryParam, pageRequest);
        }) ;

        //then
        assertTrue(exception.getMessage().contains(notInProperty));
    }

    @Test
    void success_shop_history_find_by_id(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();
        shopHistoryRepository.save(shopHistory);

        // when
        Optional<ShopHistory> findObj = shopHistoryRepository.findById(shopHistory.getDateTime()) ;

        //then
        assertTrue(findObj.isPresent());
        assertEquals(shopHistory.getDateTime(), findObj.get().getDateTime());
        assertEquals(shopHistory.getRegisterCount(), findObj.get().getRegisterCount());
        assertEquals(shopHistory.getDeleteCount(), findObj.get().getDeleteCount());
        assertEquals(shopHistory.getPayment(), findObj.get().getPayment());
        assertEquals(shopHistory.getUsed(), findObj.get().getUsed());
        assertEquals(shopHistory.getSales(), findObj.get().getSales());

    }

    @Test
    void fail_shop_history_find_by_id_from_not_exist_id(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();
        shopHistoryRepository.save(shopHistory);
        LocalDateTime notInId = now.minus(1, ChronoUnit.HOURS);

        //when
        Optional<ShopHistory> findObj = shopHistoryRepository.findById(notInId) ;

        //then
        assertFalse(findObj.isPresent());
    }


    @Test
    void success_shop_history_update(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();

        shopHistoryRepository.save(shopHistory);

        //when
        ShopHistory updateHistory = ShopHistory.builder()
                .dateTime(shopHistory.getDateTime())
                .registerCount(1)
                .deleteCount(1)
                .payment(1)
                .used(1)
                .sales(1)
                .build();
        ShopHistory updatedEntity = shopHistoryRepository.update(updateHistory);
        Optional<ShopHistory> findObj = shopHistoryRepository.findById(updatedEntity.getDateTime());

        //then
        assertTrue(findObj.isPresent());
        assertEquals(updatedEntity.getDateTime(), findObj.get().getDateTime());
        assertEquals(updatedEntity.getRegisterCount(), findObj.get().getRegisterCount());
        assertEquals(updatedEntity.getDeleteCount(), findObj.get().getDeleteCount());
        assertEquals(updatedEntity.getPayment(), findObj.get().getPayment());
        assertEquals(updatedEntity.getUsed(), findObj.get().getUsed());
        assertEquals(updatedEntity.getSales(), findObj.get().getSales());

    }

    @Test
    void fail_shop_history_update_not_exist_id(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();
        shopHistoryRepository.save(shopHistory);

        ShopHistory updateHistory = ShopHistory.builder()
                .dateTime(shopHistory.getDateTime().minus(4, ChronoUnit.HOURS))
                .sales(22)
                .build();
        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ShopHistory updateObj = shopHistoryRepository.update(updateHistory) ;
        });


        //then
        assertEquals(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), updateHistory.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)),
                exception.getMessage());
    }

    @Test
    void success_event_info_delete(){
        //given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ShopHistory shopHistory = ShopHistory.builder()
                .dateTime(now)
                .registerCount(10)
                .deleteCount(20)
                .payment(10000)
                .used(20000)
                .sales(300000)
                .build();
        shopHistoryRepository.save(shopHistory);

        //when
        shopHistoryRepository.delete(shopHistory);

        Optional<ShopHistory> findObj = shopHistoryRepository.findById(shopHistory.getDateTime());

        //then
        assertFalse(findObj.isPresent());
    }

}

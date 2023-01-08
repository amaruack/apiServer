package com.son.daou.integration.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.son.daou.config.filter.RequestFilter;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.properties.DaouConfigProperties;
import com.son.daou.service.shop.ShopHistoryService;
import com.son.daou.util.DateTimeUtils;
import com.son.daou.util.serializer.LocalDateTimeSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class InterceptorTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ShopHistoryService shopHistoryService;

    @Autowired
    DaouConfigProperties daouConfigProperties;

    @SpyBean
    RequestFilter requestFilter;

    ShopHistoryCreateRequest createRequest1;
    ShopHistoryCreateRequest createRequest2;

    @BeforeAll
    void beforeAll() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        createRequest1 = ShopHistoryCreateRequest.builder()
                .dateTime(now.format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .registerCount(10)
                .deleteCount(20)
                .payment(10000L)
                .used(20000L)
                .sales(300000L)
                .build();

        createRequest2 = ShopHistoryCreateRequest.builder()
                .dateTime(now.plus(1, ChronoUnit.HOURS).format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .registerCount(10)
                .deleteCount(20)
                .payment(10000L)
                .used(20000L)
                .sales(300000L)
                .build();

    }

    @BeforeEach
    void beforeEach() {
    }

    @Test()
    void success_rate_limit() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        int capacity = daouConfigProperties.getRateLimit().getCapacity();

        // when // then
        for (int i = 0; i < capacity; i++) {
            mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header("X-Forwarded-For", "192.168.51.1")
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andDo(print());
        }
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header("X-Forwarded-For", "192.168.51.1")
                                .queryParams(params)
                )
                .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()))
                .andDo(print());

    }

    @Test()
    void success_ip_address_access_deny() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .header("X-Forwarded-For", "10.10.0.1")
                                .queryParams(params)
                )
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andDo(print());

    }

    @Test()
    void success_ip_address_access_ok() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

}

package com.son.api.integration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.son.api.common.ErrorCode;
import com.son.api.dto.shop.ShopHistoryCreateRequest;
import com.son.api.dto.shop.ShopHistoryQueryParam;
import com.son.api.dto.shop.ShopHistoryResponse;
import com.son.api.dto.shop.ShopHistoryUpdateRequest;
import com.son.api.service.shop.ShopHistoryService;
import com.son.api.util.DateTimeUtils;
import com.son.api.util.serializer.LocalDateTimeSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ShopHistoryTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ShopHistoryService shopHistoryService;

    ShopHistoryCreateRequest createRequest1;
    ShopHistoryCreateRequest createRequest2;
    ShopHistoryUpdateRequest updateRequest1;

    ShopHistoryQueryParam searchParam ;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create();

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

        updateRequest1 = ShopHistoryUpdateRequest.builder()
                .dateTime(createRequest1.getDateTime())
                .registerCount(232)
                .deleteCount(242)
                .build();

        searchParam = ShopHistoryQueryParam.builder()
                .build();
    }

    @BeforeEach
    void beforeEach() {
    }

    @Test()
    void success_shop_history_list() throws Exception {

        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.content", hasSize(2)))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(response1.getDateTime())))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(response2.getDateTime())))
                .andDo(print());

    }

    @Test
    void success_shop_history_list_valid_query_param() throws Exception {

        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("direction", List.of("DESC"));
        params.put("sort", List.of("dateTime"));

        // when // then
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.content", hasSize(2)))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(response1.getDateTime())))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(response2.getDateTime())))
                .andDo(print());

    }

    @Test
    void fail_shop_history_list_bad_request_invalid_query_parameter() throws Exception {
        // given
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("direction", List.of("123"));
        params.put("sort", List.of("not-param"));

        // when // then
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.BAD_REQUEST.getErrorCode())))
                .andDo(print());

    }

    @Test
    void success_shop_history_find() throws Exception {
        // given
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        get("/shop-history/{id}", response1.getDateTime())
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(response1.getDateTime())))
                .andDo(print());

    }

    @Test
    void fail_shop_history_find_null_event_id() throws Exception {
        // given
//        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when //then
        mvc.perform(
                        get("/shop-history/{id}", createRequest1.getDateTime())
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), createRequest1.getDateTime()))))
                .andDo(print());

    }

    @Test
    void success_shop_history_create() throws Exception {
        // given
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        post("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(createRequest1))

                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dateTime", is(createRequest1.getDateTime())))
                .andDo(print());

    }

    @Test
    void fail_shop_history_create_mandatory_is_not_setting() throws Exception {
        // given
        ShopHistoryCreateRequest failCreateRequest = ShopHistoryCreateRequest.builder()
                .registerCount(20)
                .deleteCount(10)
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        post("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(failCreateRequest))

                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors[*].code", hasItem(ErrorCode.BAD_REQUEST.getErrorCode())))
                .andDo(print());

    }

    @Test
    void fail_shop_history_create_is_id_invalid_format_setting() throws Exception {
        // given
        String dateTime =  LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Map<String, Object> failCreateRequest = new HashMap<>();
        failCreateRequest.put("dateTime", dateTime);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        post("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(failCreateRequest))

                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(6)))
                .andDo(print());

    }

    @Test
    void fail_shop_history_create_is_invalid_parameter_setting() throws Exception {
        // given
        Map<String, Object> failCreateRequest = new HashMap<>();
        failCreateRequest.put("dateTime", LocalDateTime.now().format(DateTimeUtils.DATE_TIME_ID_FORMATTER));
        failCreateRequest.put("registerCount", "ewg");
        failCreateRequest.put("deleteCount", "ewg");
        failCreateRequest.put("payment", "ewg");
        failCreateRequest.put("used", "ewg");
        failCreateRequest.put("sales", "ewg");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        post("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(failCreateRequest))

                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[*].code", hasItem(ErrorCode.BAD_REQUEST.getErrorCode())))
                .andDo(print());

    }

    @Test
    void success_shop_history_update() throws Exception {
        // given
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        put("/shop-history/{id}", response1.getDateTime())
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(updateRequest1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(response1.getDateTime())))
                .andExpect(jsonPath("$.registerCount", is(updateRequest1.getRegisterCount())))
                .andExpect(jsonPath("$.deleteCount", is(updateRequest1.getDeleteCount())))
                .andExpect(jsonPath("$.payment", is(response1.getPayment().intValue())))
                .andExpect(jsonPath("$.used", is(response1.getUsed().intValue())))
                .andExpect(jsonPath("$.sales", is(response1.getSales().intValue())))
                .andDo(print());

    }

    @Test
    void fail_shop_history_update_not_found_shop_history() throws Exception {
        // given
        LocalDateTime failId = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minus(2, ChronoUnit.DAYS);
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        put("/shop-history/{id}", failId.format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(updateRequest1))

                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), failId.format(DateTimeUtils.DATE_TIME_ID_FORMATTER)))))
                .andDo(print());

    }

    @Test
    void success_shop_history_delete() throws Exception {
        // given
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        delete("/shop-history/{id}", response1.getDateTime())
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(response1.getDateTime())))
                .andExpect(jsonPath("$.registerCount", is(response1.getRegisterCount())))
                .andExpect(jsonPath("$.deleteCount", is(response1.getDeleteCount())))
                .andExpect(jsonPath("$.payment",  is(response1.getPayment().intValue())))
                .andExpect(jsonPath("$.used", is(response1.getUsed().intValue())))
                .andExpect(jsonPath("$.sales", is(response1.getSales().intValue())))
                .andDo(print());
        // then // verify

    }

    @Test
    void fail_shop_history_delete_not_found_shop_history() throws Exception {
        // given
        LocalDateTime failId = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minus(2, ChronoUnit.DAYS);
        ShopHistoryResponse response1 = shopHistoryService.create(createRequest1);
        ShopHistoryResponse response2 = shopHistoryService.create(createRequest2);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        delete("/shop-history/{id}", failId.format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), failId.format(DateTimeUtils.DATE_TIME_ID_FORMATTER)))))
                .andDo(print());
        // then // verify

    }

}

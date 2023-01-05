package com.son.daou.slice.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.son.daou.common.ErrorCode;
import com.son.daou.common.exception.ApiException;
import com.son.daou.controller.shop.ShopHistoryController;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import com.son.daou.service.shop.ShopHistoryService;
import com.son.daou.util.DateTimeUtils;
import com.son.daou.util.serializer.LocalDateTimeSerializer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = ShopHistoryController.class )
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopHistoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShopHistoryService service;

    ShopHistoryCreateRequest createRequest1;
    ShopHistoryCreateRequest createRequest2;
    ShopHistory history1 ;
    ShopHistory history2 ;
    ShopHistoryUpdateRequest updateRequest1;

    ShopHistoryQueryParam searchParam ;

    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create();

    @BeforeAll
    void beforeAll() {

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

    @BeforeEach
    void setUpEach() {
    }


    @Test()
    void success_shop_history_list() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        doReturn(new PageImpl(List.of(history1.toResponse(), history2.toResponse()), pageable, 2L)).when(service).search(any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when
        mvc.perform(
                get("/shop-history")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.content", hasSize(2)))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(history2.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andDo(print());

        // then // verify
        verify(service).search(any(), any());

    }

    @Test
    void success_shop_history_list_valid_query_param() throws Exception {

        Pageable pageable = PageRequest.of(0, 10);
        doReturn(new PageImpl(List.of(history1.toResponse(), history2.toResponse()), pageable, 2L)).when(service).search(any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("direction", List.of("DESC"));
        params.put("sort", List.of("dateTime"));

        // when
        mvc.perform(
                        get("/shop-history")
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.content", hasSize(2)))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andExpect(jsonPath("$._embedded.content[*].dateTime", hasItem(history2.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andExpect(jsonPath("$._links.self.href", containsString("/shop-history")))
                .andDo(print());

        // then // verify
        verify(service).search(any(), any());

    }

    @Test
    void fail_shop_history_list_bad_request_invalid_query_parameter() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        doReturn(new PageImpl(List.of(history1.toResponse(), history2.toResponse()), pageable, 2L)).when(service).search(any(), any());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("direction", List.of("123"));
        params.put("sort", List.of("not-param"));

        // when
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

        // then // verify
        verify(service, times(0)).search(any(), any());

    }
    
    @Test
    void success_shop_history_find() throws Exception {
        // given
        Mockito.doReturn(history1.toResponse()).when(service).findById(history1.getDateTime());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                    get("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andDo(print());

        // then // verify
        verify(service).findById(any());

    }

    @Test
    void fail_shop_history_find_null_event_id() throws Exception {
        // given
        doThrow(new ApiException(ErrorCode.NOT_FOUND_DATA, String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime())))
                .when(service).findById(history1.getDateTime());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when //then
        mvc.perform(
                        get("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime()))))
                .andDo(print());

    }

    @Test
    void success_shop_history_create() throws Exception {
        // given

        ShopHistoryResponse response = history1.toResponse();
        doReturn(response).when(service).create(any());
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
                .andExpect(jsonPath("$.dateTime", is(history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andDo(print());
        // then // verify
//        verify(service).findById(any());

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

        // then // verify
//        verify(service).findById(any());

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
                .andExpect(jsonPath("$.errors", hasSize(1)))
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
        ShopHistoryResponse response = ShopHistoryResponse.builder()
                .dateTime(createRequest1.getDateTime())
                .registerCount(updateRequest1.getRegisterCount())
                .deleteCount(updateRequest1.getDeleteCount())
                .payment(history1.getPayment())
                .used(history1.getUsed())
                .sales(history1.getSales())
                .build();

        doReturn(response).when(service).update(any());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        put("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(updateRequest1))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andExpect(jsonPath("$.registerCount", is(updateRequest1.getRegisterCount())))
                .andExpect(jsonPath("$.deleteCount", is(updateRequest1.getDeleteCount())))
                .andExpect(jsonPath("$.payment", is(history1.getPayment())))
                .andExpect(jsonPath("$.used", is(history1.getUsed())))
                .andExpect(jsonPath("$.sales", is(history1.getSales())))
                .andDo(print());
        // then // verify

    }

    @Test
    void fail_shop_history_update_not_found_shop_history() throws Exception {
        // given
        ShopHistoryResponse response = ShopHistoryResponse.builder()
                .registerCount(updateRequest1.getRegisterCount())
                .deleteCount(updateRequest1.getDeleteCount())
                .payment(history1.getPayment())
                .used(history1.getUsed())
                .sales(history1.getSales())
                .build();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        doThrow(new ApiException(ErrorCode.NOT_FOUND_DATA, String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .when(service).update(any());

        // when // then
        mvc.perform(
                        put("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                                .content(gson.toJson(updateRequest1))

                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)))))
                .andDo(print());
        // then // verify

    }

    @Test
    void success_shop_history_delete() throws Exception {
        // given
        ShopHistoryResponse response = history1.toResponse();

        doReturn(response).when(service).delete(history1.getDateTime());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        delete("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime", is(response.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .andExpect(jsonPath("$.registerCount", is(response.getRegisterCount())))
                .andExpect(jsonPath("$.deleteCount", is(response.getDeleteCount())))
                .andExpect(jsonPath("$.payment", is(response.getPayment())))
                .andExpect(jsonPath("$.used", is(response.getUsed())))
                .andExpect(jsonPath("$.sales", is(response.getSales())))
                .andDo(print());
        // then // verify

    }

    @Test
    void fail_shop_history_delete_not_found_shop_history() throws Exception {
        // given
        doThrow(new ApiException(ErrorCode.NOT_FOUND_DATA, String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))))
                .when(service).delete(history1.getDateTime());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        // when // then
        mvc.perform(
                        delete("/shop-history/{id}", history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER))
                                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .queryParams(params)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].code", is(ErrorCode.NOT_FOUND_DATA.getErrorCode())))
                .andExpect(jsonPath("$.errors[0].detailMessage", is(String.format(ErrorCode.NOT_FOUND_DATA.getDetailMessageFormat(), history1.getDateTime().format(DateTimeUtils.DATE_TIME_ID_FORMATTER)))))
                .andDo(print());
        // then // verify

    }

}

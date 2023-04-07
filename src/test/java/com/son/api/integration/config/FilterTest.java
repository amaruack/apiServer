package com.son.api.integration.config;

import com.son.api.config.filter.RequestFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class FilterTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    RequestFilter requestFilter;

    @BeforeAll
    void beforeAll() {

    }

    @BeforeEach
    void beforeEach() {
    }

    @Test()
    void success_filter_invoke() throws Exception {

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

        verify(requestFilter).doFilter(any(),any(),any());

    }



}

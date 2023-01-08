package com.son.daou.controller.shop;

import com.google.common.base.Strings;
import com.son.daou.common.exception.ApiException;
import com.son.daou.config.aspect.PerformanceLogging;
import com.son.daou.controller.PageableController;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import com.son.daou.service.shop.ShopHistoryService;
import com.son.daou.util.DateTimeUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

@RequestMapping(value = "shop-history",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RestController
public class ShopHistoryController extends PageableController<ShopHistoryResponse> {

    private final ShopHistoryService service;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    public ShopHistoryController(ShopHistoryService service){
        this.service = service;
    }

    @GetMapping(value = "", consumes = MediaType.ALL_VALUE)
    @Operation(summary = "데이터 검색 조회", description = "검색정보를 토대로 정보를 조회하여 응답 합니다.")
    public ResponseEntity<PagedModel<EntityModel<ShopHistoryResponse>>> search(
            @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, example = "2023-01-07T00")
            @RequestParam(name = "startDatetime", required = false) String startDatetime,
            @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, example = "2023-01-07T23")
            @RequestParam(name = "endDatetime", required = false) String endDatetime,

            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "24") Integer size, // rows
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction,  // sort direction, DESC, ASC
            @RequestParam(name = "sort", required = false) String[] sort  // sort
    ) throws ApiException {

        Pageable pageable = getPageable(page, size, direction, sort);

        ShopHistoryQueryParam queryParam = ShopHistoryQueryParam.builder()
                .startDatetime(Strings.isNullOrEmpty(startDatetime) ? null: LocalDateTime.parse(startDatetime, DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .endDatetime(Strings.isNullOrEmpty(endDatetime) ? null: LocalDateTime.parse(endDatetime, DateTimeUtils.DATE_TIME_ID_FORMATTER))
                .build();

        Page<ShopHistoryResponse> pages = service.search(queryParam, pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pages));
    }

    @PostMapping(value = "")
    @Operation(summary = "등록 API", description = "입력한 정보를 이용하여 데이터를 등록합니다.")
    public ResponseEntity<ShopHistoryResponse> create(
            @RequestBody @Validated ShopHistoryCreateRequest createRequest
    ) {
        ShopHistoryResponse response = service.create(createRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(value = "{id}", consumes = MediaType.ALL_VALUE)
    @Operation(summary = "데이터 정보 조회", description = "해당 데이터 정보를 조회 합니다.")
    public HttpEntity<ShopHistoryResponse> find(
            @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, example = "2023-01-07T00")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") String id
    ) throws ApiException {
        ShopHistoryResponse response = service.findById(LocalDateTime.parse(id, DateTimeUtils.DATE_TIME_ID_FORMATTER));
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "{id}" )
    @Operation(summary = "데이터 정보 수정", description = "데이터 정보를 수정합니다.")
    public ResponseEntity<ShopHistoryResponse> update(
            @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, example = "2023-01-07T00")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") String id,
            @RequestBody @Validated ShopHistoryUpdateRequest updateRequest
    ) throws ApiException {
        updateRequest.setDateTime(id);
        ShopHistoryResponse response = service.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "{id}" )
    @Operation(summary = "데이터 정보 삭제", description = "데이터 정보를 삭제합니다." )
    public ResponseEntity<ShopHistoryResponse> delete(
            @Schema(pattern = DateTimeUtils.DATE_TIME_ID_REGEX, example = "2023-01-07T00")
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") String id
    ) throws ApiException {
        ShopHistoryResponse response = service.delete(LocalDateTime.parse(id, DateTimeUtils.DATE_TIME_ID_FORMATTER));
        return ResponseEntity.ok(response);
    }

}

package com.son.daou.controller.shop;

import com.son.daou.common.exception.ApiException;
import com.son.daou.config.aspect.PerformanceLogging;
import com.son.daou.controller.PageableController;
import com.son.daou.dto.shop.ShopHistoryCreateRequest;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import com.son.daou.dto.shop.ShopHistoryResponse;
import com.son.daou.dto.shop.ShopHistoryUpdateRequest;
import com.son.daou.service.shop.ShopHistoryService;
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

import java.time.LocalDateTime;

@RequestMapping(value = "shop-history",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RestController
public class ShopHistoryController extends PageableController<ShopHistoryResponse> {

    private final ShopHistoryService service;

    @Autowired
    public ShopHistoryController(ShopHistoryService service){
        this.service = service;
    }

    @GetMapping(value = "")
    public ResponseEntity<PagedModel<EntityModel<ShopHistoryResponse>>> search(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @RequestParam(name = "startDatetime", required = false) LocalDateTime startDatetime,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @RequestParam(name = "endDatetime", required = false) LocalDateTime endDatetime,

            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "24") Integer size, // rows
            @RequestParam(name = "direction", required = false, defaultValue = "DESC") String direction,  // sort direction, DESC, ASC
            @RequestParam(name = "sort", required = false) String[] sort  // sort
    ) throws ApiException {

        Pageable pageable = getPageable(page, size, direction, sort);

        ShopHistoryQueryParam queryParam = ShopHistoryQueryParam.builder()
                .startDatetime(startDatetime)
                .endDatetime(endDatetime)
                .build();

        Page<ShopHistoryResponse> pages = service.search(queryParam, pageable);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(pages));
    }

    @PostMapping("")
    public ResponseEntity<ShopHistoryResponse> create(
            @RequestBody @Validated ShopHistoryCreateRequest createRequest
    ) {
        ShopHistoryResponse response = service.create(createRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(value = "{id}")
    public HttpEntity<ShopHistoryResponse> find(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") LocalDateTime id
    ) throws ApiException {
        ShopHistoryResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "{id}" )
    public ResponseEntity<ShopHistoryResponse> update(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") LocalDateTime id,
            @RequestBody @Validated ShopHistoryUpdateRequest updateRequest
    ) throws ApiException {
        updateRequest.setDateTime(id);
        ShopHistoryResponse response = service.update(updateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "{id}" )
    public ResponseEntity<ShopHistoryResponse> delete(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH")
            @PathVariable(name = "id") LocalDateTime id
    ) throws ApiException {
        ShopHistoryResponse response = service.delete(id);
        return ResponseEntity.ok(response);
    }

}

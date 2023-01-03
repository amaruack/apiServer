package com.son.daou.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public abstract class PageableController {

    public Pageable getPageable(Integer page, Integer size, String direction, String[] sort){
        // sort direction, DESC, ASC
        Pageable pageable;
        if (Objects.isNull(sort) || sort.length == 0) {
            pageable = PageRequest.of(page, size);
        } else {
            pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        }
        return pageable;
    }
}
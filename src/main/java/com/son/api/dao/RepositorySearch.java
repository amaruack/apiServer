package com.son.api.dao;

import com.son.api.domain.DaouEntity;
import com.son.api.dto.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepositorySearch<T extends DaouEntity, Q extends QueryParam> {
    Page<T> search(Q queryParam, Pageable pageable);
}
package com.son.daou.dao;

import com.son.daou.domain.DaouEntity;
import com.son.daou.dto.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RepositorySearch<T extends DaouEntity, Q extends QueryParam> {
    Page<T> search(Q queryParam, Pageable pageable);
}
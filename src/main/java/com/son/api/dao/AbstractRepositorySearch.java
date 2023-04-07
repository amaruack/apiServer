package com.son.api.dao;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.son.api.domain.DaouEntity;
import com.son.api.dto.QueryParam;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractRepositorySearch<T extends DaouEntity, Q extends QueryParam> implements RepositorySearch<T, Q> {

    protected <V> BooleanExpression condition(V value, Function<V, BooleanExpression> function) {
        return Optional.ofNullable(value).map(function).orElse(null);
    }

    protected OrderSpecifier[] orderCondition(Class<T> clazz, String variable, Pageable pageable) {
        PathBuilder<T> entityPath = new PathBuilder<>(clazz, variable);
        OrderSpecifier[] tmp = pageable.getSort()
                .stream()
                .map(order -> new OrderSpecifier(Order.valueOf(order.getDirection().name()), entityPath.get(order.getProperty())))
                .toArray(OrderSpecifier[]::new);
        return tmp ;
    }

    public long getTotalCount(JPAQueryFactory jpaQueryFactory, EntityPathBase<T> root, Q queryParam){
        return jpaQueryFactory
            .select(root.count())
            .from(root)
            .where(createWhere(queryParam))
            .fetchOne();
    }

    public abstract BooleanExpression[] createWhere(Q queryParam);

}

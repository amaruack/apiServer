package com.son.daou.dao.shop;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.son.daou.dao.AbstractRepositorySearch;
import com.son.daou.domain.shop.QShopHistory;
import com.son.daou.domain.shop.ShopHistory;
import com.son.daou.dto.shop.ShopHistoryQueryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class ShopHistoryRepositoryImpl extends AbstractRepositorySearch<ShopHistory, ShopHistoryQueryParam> {

    private final JPAQueryFactory jpaQueryFactory;
    private final QShopHistory root = QShopHistory.shopHistory;

    @Override
    public Page<ShopHistory> search(ShopHistoryQueryParam queryParam, Pageable pageable) {
        List<ShopHistory> fetched = jpaQueryFactory
                .select(root)
                .from(root)
                .where(
                    createWhere(queryParam)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderCondition(ShopHistory.class, root.toString(), pageable))
                .fetch();

        long totalSize = getTotalCount(jpaQueryFactory, root, queryParam);

        return new PageImpl<>(fetched, pageable, totalSize);
    }

    public BooleanExpression[] createWhere(ShopHistoryQueryParam queryParam){
        return new BooleanExpression[]{
            condition(queryParam.getStartDatetime(), root.dateTime :: goe),
            condition(queryParam.getEndDatetime(), root.dateTime :: lt ),
        };
    }
}

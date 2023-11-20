package com.zerobase.reserve.domain.store.dto;

import com.zerobase.reserve.domain.common.validator.ValidEnum;
import com.zerobase.reserve.domain.store.type.OrderType;
import com.zerobase.reserve.domain.store.type.SortType;

public record SortCondition(
        @ValidEnum(enumClass = SortType.class)
        SortType sortType,

        @ValidEnum(enumClass = OrderType.class)
        OrderType orderType
) {
}

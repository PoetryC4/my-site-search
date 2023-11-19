package com.hzy.es.controller.search;

import com.hzy.es.common.ErrorCode;
import com.hzy.es.controller.search.impl.SearchStrategyPostDefault;
import com.hzy.es.controller.search.impl.SearchStrategyPostEs;
import com.hzy.es.controller.search.impl.SearchStrategyUserDefault;
import com.hzy.es.controller.search.impl.SearchStrategyUserEs;
import com.hzy.es.exception.BusinessException;
import com.hzy.es.model.enums.SearchTypeEnum;

public class SearchFactory {
    public static SearchStrategy newInstance(String category, Boolean userEs) {
        if (userEs) {
            if (SearchTypeEnum.USER.getValue().equals(category)) {
                return new SearchStrategyUserEs();
            } else if (SearchTypeEnum.POST.getValue().equals(category)) {
                return new SearchStrategyPostEs();
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        } else {
            if (SearchTypeEnum.USER.getValue().equals(category)) {
                return new SearchStrategyUserDefault();
            } else if (SearchTypeEnum.POST.getValue().equals(category)) {
                return new SearchStrategyPostDefault();
            } else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
}

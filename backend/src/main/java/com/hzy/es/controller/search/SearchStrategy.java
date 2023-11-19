package com.hzy.es.controller.search;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.es.model.dto.search.CommonQueryRequest;

import javax.servlet.http.HttpServletRequest;

public interface SearchStrategy<T> {
    public Page<T> doSearch(CommonQueryRequest commonQueryRequest, HttpServletRequest request);
}

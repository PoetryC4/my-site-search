package com.hzy.es.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.gson.Gson;
import com.hzy.es.common.BaseResponse;
import com.hzy.es.common.ErrorCode;
import com.hzy.es.common.ResultUtils;
import com.hzy.es.controller.search.SearchFactory;
import com.hzy.es.controller.search.SearchStrategy;
import com.hzy.es.exception.BusinessException;
import com.hzy.es.exception.ThrowUtils;
import com.hzy.es.model.dto.search.CommonQueryRequest;
import com.hzy.es.service.PostService;
import com.hzy.es.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.hzy.es.utils.SqlUtils.isAnyNull;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @PostMapping("/list")
    public BaseResponse<IPage> listObjectByPage(@RequestBody CommonQueryRequest commonQueryRequest,
                                                HttpServletRequest request) {
        if (commonQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = commonQueryRequest.getCurrent();
        long size = commonQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 25, ErrorCode.PARAMS_ERROR);
        String category = commonQueryRequest.getCategory();
        Boolean userEs = commonQueryRequest.getUserEs();
        if (isAnyNull(category, userEs)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        SearchStrategy searchStrategy = SearchFactory.newInstance(category, userEs);
        return ResultUtils.success(searchStrategy.doSearch(commonQueryRequest, request));
    }
}

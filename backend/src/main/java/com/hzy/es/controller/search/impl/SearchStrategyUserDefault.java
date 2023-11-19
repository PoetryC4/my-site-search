package com.hzy.es.controller.search.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.es.controller.search.SearchStrategy;
import com.hzy.es.model.dto.search.CommonQueryRequest;
import com.hzy.es.model.dto.user.UserQueryRequest;
import com.hzy.es.model.entity.User;
import com.hzy.es.model.vo.UserVO;
import com.hzy.es.service.PostService;
import com.hzy.es.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class SearchStrategyUserDefault implements SearchStrategy {
    private static SearchStrategyUserDefault searchStrategyUserDefault;

    @Resource
    private UserService userService;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        searchStrategyUserDefault = this;
        searchStrategyUserDefault.userService= this.userService;
        // 初使化时将已静态化的testService实例化
    }
    @Override
    public Page<UserVO> doSearch(CommonQueryRequest commonQueryRequest, HttpServletRequest request) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        BeanUtils.copyProperties(commonQueryRequest, userQueryRequest);
        userQueryRequest.setUserName(commonQueryRequest.getSearchText());

        Page<User> userPage = searchStrategyUserDefault.userService.page(new Page<>(commonQueryRequest.getCurrent(), commonQueryRequest.getPageSize()),
                searchStrategyUserDefault.userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(commonQueryRequest.getCurrent(), commonQueryRequest.getPageSize(), userPage.getTotal());
        List<UserVO> userVO = searchStrategyUserDefault.userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return userVOPage;
    }
}

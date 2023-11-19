package com.hzy.es.controller.search.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzy.es.common.ResultUtils;
import com.hzy.es.controller.search.SearchStrategy;
import com.hzy.es.model.dto.post.PostQueryRequest;
import com.hzy.es.model.dto.search.CommonQueryRequest;
import com.hzy.es.model.entity.Post;
import com.hzy.es.model.vo.PostVO;
import com.hzy.es.service.PostService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class SearchStrategyPostEs implements SearchStrategy {
    private static SearchStrategyPostEs searchStrategyPostEs;

    @Resource
    private PostService postService;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        searchStrategyPostEs = this;
        searchStrategyPostEs.postService= this.postService;
        // 初使化时将已静态化的testService实例化
    }
    @Override
    public Page<PostVO> doSearch(CommonQueryRequest commonQueryRequest, HttpServletRequest request) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        BeanUtils.copyProperties(commonQueryRequest, postQueryRequest);
        postQueryRequest.setSearchText(commonQueryRequest.getSearchText());

        Page<Post> postPage = searchStrategyPostEs.postService.searchFromEs(postQueryRequest);
        Page<PostVO> postVOPage = searchStrategyPostEs.postService.getPostVOPage(postPage, request);
        return postVOPage;
    }
}

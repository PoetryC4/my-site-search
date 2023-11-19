package com.hzy.es.job.once;

import com.hzy.es.esdao.PostEsDao;
import com.hzy.es.esdao.UserEsDao;
import com.hzy.es.model.dto.post.PostEsDTO;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.hzy.es.model.dto.user.UserEsDTO;
import com.hzy.es.model.entity.Post;
import com.hzy.es.model.entity.User;
import com.hzy.es.service.PostService;
import com.hzy.es.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 全量同步帖子到 es
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class FullSyncPostToEs implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Resource
    private PostEsDao postEsDao;
    @Resource
    private UserService userService;

    @Resource
    private UserEsDao userEsDao;

    @Override
    public void run(String... args) {
        final int pageSize = 500;
        List<Post> postList = postService.list();
        if (!CollectionUtils.isEmpty(postList)) {
            List<PostEsDTO> postEsDTOList = postList.stream().map(PostEsDTO::objToDto).collect(Collectors.toList());
            int total = postEsDTOList.size();
            log.info("FullSyncPostToEs start, total {}", total);
            for (int i = 0; i < total; i += pageSize) {
                int end = Math.min(i + pageSize, total);
                log.info("sync from {} to {}", i, end);
                postEsDao.saveAll(postEsDTOList.subList(i, end));
            }
            log.info("FullSyncPostToEs end, total {}", total);
        }
        List<User> userList = userService.list();
        if (!CollectionUtils.isEmpty(userList)) {
            List<UserEsDTO> userEsDTOList = userList.stream().map(UserEsDTO::objToDto).collect(Collectors.toList());
            int total = userEsDTOList.size();
            log.info("FullSyncUserToEs start, total {}", total);
            for (int i = 0; i < total; i += pageSize) {
                int end = Math.min(i + pageSize, total);
                log.info("sync from {} to {}", i, end);
                userEsDao.saveAll(userEsDTOList.subList(i, end));
            }
            log.info("FullSyncUserToEs end, total {}", total);
        }
    }
}

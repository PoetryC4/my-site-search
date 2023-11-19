package com.hzy.es.job.cycle;

import com.hzy.es.esdao.PostEsDao;
import com.hzy.es.esdao.UserEsDao;
import com.hzy.es.mapper.PostMapper;
import com.hzy.es.mapper.UserMapper;
import com.hzy.es.model.dto.post.PostEsDTO;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.hzy.es.model.dto.user.UserEsDTO;
import com.hzy.es.model.entity.Post;
import com.hzy.es.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 增量同步帖子到 es
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncPostToEs {

    @Resource
    private PostMapper postMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private PostEsDao postEsDao;
    @Resource
    private UserEsDao userEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        final int pageSize = 500;
        List<Post> postList = postMapper.listPostWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(postList)) {
            log.info("no inc post");
        } else {
            List<PostEsDTO> postEsDTOList = postList.stream()
                    .map(PostEsDTO::objToDto)
                    .collect(Collectors.toList());
            int total1 = postEsDTOList.size();
            log.info("IncSyncPostToEs start, total {}", total1);
            for (int i = 0; i < total1; i += pageSize) {
                int end = Math.min(i + pageSize, total1);
                log.info("sync from {} to {}", i, end);
                postEsDao.saveAll(postEsDTOList.subList(i, end));
            }
            log.info("IncSyncPostToEs end, total {}", total1);
        }
        List<User> userList = userMapper.listUserWithDelete(fiveMinutesAgoDate);
        if (CollectionUtils.isEmpty(userList)) {
            log.info("no inc user");
        } else {
            List<UserEsDTO> userEsDTOList = userList.stream()
                    .map(UserEsDTO::objToDto)
                    .collect(Collectors.toList());
            int total2 = userEsDTOList.size();
            log.info("IncSyncUserToEs start, total {}", total2);
            for (int i = 0; i < total2; i += pageSize) {
                int end = Math.min(i + pageSize, total2);
                log.info("sync from {} to {}", i, end);
                userEsDao.saveAll(userEsDTOList.subList(i, end));
            }
            log.info("IncSyncPostToEs end, total {}", total2);
        }
    }
}

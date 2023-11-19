package com.hzy.es.esdao;

import com.hzy.es.model.dto.post.PostEsDTO;
import com.hzy.es.model.dto.user.UserEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface UserEsDao extends ElasticsearchRepository<UserEsDTO, Long> {

    List<UserEsDTO> findByUserName(String userName);
}
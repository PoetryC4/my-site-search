package com.hzy.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzy.es.model.entity.Post;
import com.hzy.es.model.entity.User;

import java.util.Date;
import java.util.List;

/**
 * 用户数据库操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> listUserWithDelete(Date minUpdateTime);

}





package com.carry.wifi_monitor.mapper;

import com.carry.wifi_monitor.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/12.
 */

@Repository
public interface UserDao extends CrudRepository<User,Long>{

    public User findByUsernameAndPassword(String username, String password);

}

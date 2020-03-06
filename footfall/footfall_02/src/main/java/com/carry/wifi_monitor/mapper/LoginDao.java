package com.carry.wifi_monitor.mapper;


import com.carry.wifi_monitor.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by huangds on 2017/10/28.
 */
@Repository
public interface LoginDao extends CrudRepository<User, Long> {

  public List<User> findByUsernameAndPassword(String name, String password);
}

package com.carry.wifi_monitor.service.impl;

import com.carry.wifi_monitor.entity.User;
import com.carry.wifi_monitor.mapper.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangds on 2017/10/28.
 */
@Service
public class LoginService {

    @Autowired
    private LoginDao loginDao;

  public boolean verifyLogin(User user){

      //System.out.println("111111111111111111111111111111111111");
     List<User> userList = loginDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
      return userList.size()>0;
  }

    public void save(User user){
        loginDao.save(user);

    }

}

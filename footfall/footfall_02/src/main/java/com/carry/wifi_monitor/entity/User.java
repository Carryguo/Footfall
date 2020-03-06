package com.carry.wifi_monitor.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * Created by huangds on 2017/10/28.
 */
@Entity
@Table(name="table_user")
@Data
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;




}

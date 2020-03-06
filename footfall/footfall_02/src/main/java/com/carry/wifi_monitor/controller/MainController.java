package com.carry.wifi_monitor.controller;

import com.carry.wifi_monitor.entity.Main;
import com.carry.wifi_monitor.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private MainService mainService;
    @GetMapping("/list")
    public List<String> selectAll(){
        return mainService.selectAll();
    }

    @PostMapping("/insert")
    public void insert(Main main){
        mainService.insert(main);
    }
}
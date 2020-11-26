package com.github.bruce95a.file.share.controller;

import com.github.bruce95a.file.share.entity.Cfg;
import com.github.bruce95a.file.share.entity.User;
import com.github.bruce95a.file.share.service.CfgService;
import com.github.bruce95a.file.share.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CfgController {
    private static final Logger logger = LoggerFactory.getLogger(CfgController.class);

    @Autowired
    private CfgService cfgService;

    @Autowired
    private FileService fileService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        logger.info(user.getUser());
        String userName = cfgService.getUserName();
        String userPassword = cfgService.getUserPassword();
        if (userName != null && userPassword != null && userName.equals(user.getUser()) && userPassword.equals(user.getPsw())) {
            HttpSession session = request.getSession();
            session.setAttribute("User", "user");
            ServletContext context = request.getServletContext();
            context.setAttribute("User", "user");
        } else {
            result.put("msg", "用户名或密码不正确");
        }
        return result;
    }

    @GetMapping("/config")
    public Cfg getCfg() {
        Cfg cfg = new Cfg();
        cfg.setAddress(cfgService.getSiteAddress());
        cfg.setStore(cfgService.getStorePath());
        return cfg;
    }

    @PostMapping("/config")
    public Map<String, Object> udtCfg(@RequestBody Cfg cfg) {
        Map<String, Object> result = new HashMap<>();
        File file = new File(cfg.getStore());
        if (!file.exists()) {
            result.put("msg", "报告保存目录不存在");
            return result;
        }
        if (!file.isDirectory()) {
            result.put("msg", "报告保存目录不是目录");
            return result;
        }
        if (!file.canWrite()) {
            result.put("msg", "报告保存目录不可写");
            return result;
        }
        if (!cfgService.setStorePath(cfg.getStore())) {
            result.put("msg", "发生错误");
            return result;
        }
        if (!cfgService.setSiteAddress(cfg.getAddress())) {
            result.put("msg", "发生错误");
            return result;
        }
        fileService.rescan();
        return result;
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("User");
        ServletContext context = request.getServletContext();
        context.removeAttribute("User");
    }

    @GetMapping("/profile")
    public User getProfile() {
        User user = new User();
        user.setUser(cfgService.getUserName());
        return user;
    }

    @PostMapping("/profile")
    public Map<String, Object> udtUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        String userPassword = cfgService.getUserPassword();
        if (userPassword != null && !userPassword.equals(user.getPsw())) {
            result.put("msg", "原密码不正确");
            return result;
        }
        if (!cfgService.setUserName(user.getUser())) {
            result.put("msg", "发生错误");
            return result;
        }
        if (!cfgService.setUserPassword(user.getPswNew())) {
            result.put("msg", "发生错误");
            return result;
        }
        return result;
    }
}

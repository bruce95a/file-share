package com.github.bruce95a.file.share.controller;

import com.github.bruce95a.file.share.service.ConfigService;
import com.github.bruce95a.file.share.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

@Controller
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String showConfig(Model model) {
        model.addAttribute("store", configService.getStorePath());
        model.addAttribute("address", configService.getSiteAddress());
        return "config";
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public String config(@RequestParam("store") String path,
                         @RequestParam("address") String address,
                         Model model) {
        model.addAttribute("store", path);
        model.addAttribute("address", address);
        File file = new File(path);
        if (!file.exists()) {
            model.addAttribute("msg", "报告保存目录不存在");
            return "config";
        }
        if (!file.isDirectory()) {
            model.addAttribute("msg", "报告保存目录不是目录");
            return "config";
        }
        if (!file.canWrite()) {
            model.addAttribute("msg", "报告保存目录不可写");
            return "config";
        }
        if (!configService.setStorePath(path)) {
            model.addAttribute("msg", "发生错误");
            return "config";
        }
        if (!configService.setSiteAddress(address)) {
            model.addAttribute("msg", "发生错误");
            return "config";
        }
        fileService.rescan();
        return "redirect:/report";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("user") String name,
                        @RequestParam("psw") String password,
                        HttpServletRequest request, Model model) {
        model.addAttribute("name", name);
        String userName = configService.getUserName();
        if (userName == null) {
            model.addAttribute("msg", "用户不存在");
            return "index";
        }
        String userPassword = configService.getUserPassword();
        if (userPassword != null && !userPassword.equals(password)) {
            model.addAttribute("msg", "密码不正确");
            return "index";
        }
        HttpSession session = request.getSession();
        session.setAttribute("User", "user");
        ServletContext context = request.getServletContext();
        context.setAttribute("User", "user");
        return "redirect:/report";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("User");
        ServletContext context = request.getServletContext();
        context.removeAttribute("User");
        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam("user") String name,
                           @RequestParam("psw") String password,
                           @RequestParam("psw2") String password2,
                           HttpServletRequest request, Model model) {
        model.addAttribute("name", name);
        if (!password.equals(password2)) {
            model.addAttribute("msg", "密码不一致");
            return "register";
        }
        if (!configService.setUserName(name)) {
            model.addAttribute("msg", "发生错误");
            return "register";
        }
        if (!configService.setUserPassword(password)) {
            model.addAttribute("msg", "发生错误");
            return "register";
        }
        HttpSession session = request.getSession();
        session.setAttribute("User", "user");
        return "redirect:/report";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model) {
        String name = configService.getUserName();
        model.addAttribute("name", name);
        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String modifyProfile(@RequestParam("user") String name,
                                @RequestParam("psw0") String password,
                                @RequestParam("psw1") String password1,
                                @RequestParam("psw2") String password2,
                                Model model) {
        model.addAttribute("name", name);
        String userPassword = configService.getUserPassword();
        if (userPassword != null && !userPassword.equals(password)) {
            model.addAttribute("msg", "原密码不正确");
            return "profile";
        }
        if (!password1.equals(password2)) {
            model.addAttribute("msg", "密码不一致");
            return "profile";
        }
        if (!configService.setUserName(name)) {
            model.addAttribute("msg", "发生错误");
            return "profile";
        }
        if (!configService.setUserPassword(password1)) {
            model.addAttribute("msg", "发生错误");
            return "profile";
        }
        return "redirect:/report";
    }
}

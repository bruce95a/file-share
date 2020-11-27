package com.github.bruce95a.file.share.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "upload";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String info(@RequestParam("l") Optional<String> flag, Model model) {
        if ("1".equals(flag.orElse("0"))) {
            model.addAttribute("msg", "你已在另一处登录，请注销后重试");
        }
        return "info";
    }
}

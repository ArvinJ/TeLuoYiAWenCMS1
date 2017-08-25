package com.lgq.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liguoqing on 2016/1/11.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/main")
    public String main() {
        return "main";
    }
}

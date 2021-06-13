package com.haulmont.testtask.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @GetMapping(value = "/")
    public ModelAndView test() {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/admin")
    public ModelAndView test1() {
        return new ModelAndView("admin");
    }
}

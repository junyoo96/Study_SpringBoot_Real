package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
// log를 사용하기 위해 필요
@Slf4j
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.info(("home controller"));
        return "home";
    }
}

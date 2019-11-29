package com.learn.page.controller;

import com.learn.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 张惊涛 on 2019/11/29 11:07
 * @Description:
 */
@RestController
@RequestMapping("page")
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("list")
    public Object queryList(@RequestParam(value = "page",defaultValue = "1") Integer page,
                            @RequestParam(value = "limit",defaultValue = "5") Integer limit){
        return pageService.queryListByPage(page,limit);
    }
}

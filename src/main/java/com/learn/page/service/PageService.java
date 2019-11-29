package com.learn.page.service;

import com.learn.page.mapper.PageMapper;
import com.learn.page.util.PageUtil;
import com.learn.page.util.ResponseUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Service
public class PageService {

    @Resource
    private PageMapper pageMapper;


    public ResponseUtil queryListByPage(Integer page, Integer limit) {
        Map<String,Object> map =new HashMap<>();
        PageUtil pageUtil = new PageUtil(page,limit);
        map.put("page",pageUtil);
        return ResponseUtil.success().data(pageMapper.queryListByPage(map)).set("count",pageUtil.getCount());
    }
}

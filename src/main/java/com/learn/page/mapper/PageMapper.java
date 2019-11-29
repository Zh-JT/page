package com.learn.page.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface PageMapper {

    List<Map<String,Object>> queryListByPage(Map<String, Object> map);

}

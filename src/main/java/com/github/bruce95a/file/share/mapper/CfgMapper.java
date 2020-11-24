package com.github.bruce95a.file.share.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CfgMapper {

    @Update("UPDATE config SET value=#{value} WHERE name=#{name}")
    int updateValue(String name, String value);

    @Select("SELECT value FROM config WHERE name=#{name}")
    String selectValue(String name);

}

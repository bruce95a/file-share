package com.github.bruce95a.file.share.mapper;

import com.github.bruce95a.file.share.entity.ShareFile;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface FileMapper {

    @Insert("INSERT INTO report (uuid, name, datetime) VALUES(#{uuid}, #{name}, #{datetime})")
    int insert(ShareFile shareFile);

    @Update("UPDATE report SET last = NULL WHERE name=#{name}")
    int updateName(ShareFile shareFile);

    @Update("UPDATE report SET last=#{last}")
    int updateLast(String last);

    @Select("SELECT name FROM report WHERE uuid=#{uuid}")
    String selectName(String uuid);

    @Delete("DELETE FROM report WHERE uuid=#{uuid}")
    int delete(String uuid);

    @Delete("DELETE FROM report WHERE last=#{last}")
    int deleteLast(String last);

    @Select("SELECT * FROM report ORDER BY datetime DESC LIMIT #{index}, 10")
    List<ShareFile> selectAll(int index);

    @Select("SELECT count(*) FROM report")
    int selectAllCount();
}

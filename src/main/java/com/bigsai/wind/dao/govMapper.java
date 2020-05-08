package com.bigsai.wind.dao;

import com.bigsai.wind.pojo.gov;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface govMapper {
    @Select("select url from urlname where name=#{name}")
    public String getOriginalurl(String name);

    @Insert("insert into urlname (name,url) values(#{name},#{url})")
    public boolean inserturl(@Param("name") String name, @Param("url") String url);

    @Select("select * from gov where url=#{url}")
    public gov getgovbyurl(String url);


    @Select("select id,case_number,title,content,url,release_time,operation_time from gov")
    public List<gov>getall();

    public void insertgov(gov gov);
    public boolean updategov(gov gov);



}

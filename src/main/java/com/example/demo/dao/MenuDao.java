package com.example.demo.dao;

import com.example.demo.vo.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by T011689 on 2018/12/24.
 */
public interface MenuDao {
    public MenuVo selectMenuWithRole(@Param(value = "menuUrl") String menuUrl);
}

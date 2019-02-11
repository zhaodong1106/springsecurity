package com.example.demo.vo;

import com.example.demo.entity.Role;

import java.util.Date;
import java.util.List;

/**
 * Created by T011689 on 2018/12/24.
 */
public class MenuVo {
    private Integer menuId;
    private String menuUrl;
    private String menuName;
    private Date createTime;
    private List<Role> roles;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}

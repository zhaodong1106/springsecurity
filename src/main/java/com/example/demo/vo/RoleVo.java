package com.example.demo.vo;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by T011689 on 2018/10/24.
 */
public class RoleVo implements GrantedAuthority {

    private static final long serialVersionUID = -4125052146006013195L;
    private int roleId;
    private String role;
    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleVo roleVo = (RoleVo) o;

        return role.equals(roleVo.role);

    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }
}

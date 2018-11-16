package com.example.demo.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.List;

/**
 * Created by T011689 on 2018/10/24.
 */
public class StudentVo implements UserDetails {
    private static final long serialVersionUID = 8342172521316445842L;
    private Integer id;
    private String name;
    private  String password;
    private String email;
    private List<RoleVo> roleVos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentVo studentVo = (StudentVo) o;

        return name.equals(studentVo.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleVos;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoleVo> getRoleVos() {
        return roleVos;
    }

    public void setRoleVos(List<RoleVo> roleVos) {
        this.roleVos = roleVos;
    }
}

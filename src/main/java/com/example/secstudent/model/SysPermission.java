package com.example.secstudent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class SysPermission implements GrantedAuthority {
    private Long id;
    private String name;
    private String code;
    private String url;
    private String method;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.code + ":" + this.method.toUpperCase();
    }
}
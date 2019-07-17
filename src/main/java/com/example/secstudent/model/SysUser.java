package com.example.secstudent.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class SysUser {
    private Long id;
    private String username;
    private String password;

    private List<SysPermission> sysPermissions;
}
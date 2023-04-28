package com.example.bloglv3.user.dto;

import com.example.bloglv3.user.entity.UserRoleEnum;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String username;
    private String password;
    private UserRoleEnum userRole;
}

package com.example.bloglv3.user.dto;


import com.example.bloglv3.user.entity.UserRoleEnum;
import lombok.Getter;
import jakarta.validation.constraints.Pattern;


@Getter
public class SignupRequestDto {
    @Pattern(regexp = "^[0-9a-z]{4,10}$", message = "4 ~ 10자 사이의 알파벳 소문자와 숫자만 가능합니다.")
    private String username;

    @Pattern(regexp = "^[0-9a-zA-Z]{8,15}$", message = "8 ~ 15자 사이의 알파벳 대소문자와 숫자만 가능합니다.")
    private String password;
    private UserRoleEnum userRole;
    private String adminToken;
}





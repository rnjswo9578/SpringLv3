package com.example.bloglv3.user.service;

import com.example.bloglv3.global.jwt.JwtUtil;
import com.example.bloglv3.user.dto.LoginRequestDto;
import com.example.bloglv3.user.dto.SignupRequestDto;
import com.example.bloglv3.user.entity.User;
import com.example.bloglv3.user.entity.UserRoleEnum;
import com.example.bloglv3.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public ResponseEntity<String> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 회원이 있습니다!");
        }

        if(signupRequestDto.getUserRole().equals(UserRoleEnum.ADMIN)){
            if(!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)){
                throw new IllegalArgumentException("관리자 키가 틀립니다");
            }
        }

        userRepository.saveAndFlush(new User(signupRequestDto));

        if (userRepository.findByUsername(username).isPresent()) {
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("저장 실패!", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<String> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername(), user.getUserRole());
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }
}

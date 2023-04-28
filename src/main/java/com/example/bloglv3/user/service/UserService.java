package com.example.bloglv3.user.service;

import com.example.bloglv3.global.jwt.JwtUtil;
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


    @Transactional
    public ResponseEntity signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
//        String password = signupRequestDto.getPassword();
//        아이디 비밀번호 유효성 확인
//        @Pattern 같은 어노테이션으로 가능
//        if (username.length() < 4 || username.length() > 10 || !Pattern.matches("[a-z0-9]*$", username)) {
//            throw new IllegalArgumentException("이미 회원이 있습니다!");
//        }
//        if (password.length() < 8 || password.length() > 15 || !Pattern.matches("[A-Za-z0-9]*$", password)) {
//            throw new IllegalArgumentException("이미 회원이 있습니다!");
//        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 회원이 있습니다!");
        }

        userRepository.saveAndFlush(new User(signupRequestDto));

        if (userRepository.findByUsername(username).isPresent()) {
            return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("저장 실패!", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity login(SignupRequestDto signupRequestDto, HttpServletResponse httpServletResponse) {
        String username = signupRequestDto.getUsername();
        UserRoleEnum role = signupRequestDto.getUserRole();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        String token = jwtUtil.createToken(username, role);
        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }
}

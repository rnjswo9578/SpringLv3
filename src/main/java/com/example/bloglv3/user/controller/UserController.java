package com.example.bloglv3.user.controller;


import com.example.bloglv3.user.dto.SignupRequestDto;
import com.example.bloglv3.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    @GetMapping("/signup")
//    public ModelAndView getSignupPage() {
//        return new ModelAndView("signup.html");
//    }
//
//    @GetMapping("/login")
//    public ModelAndView getLoginPage() {
//        return new ModelAndView("login.html");
//    }

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody SignupRequestDto signupRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(signupRequestDto, httpServletResponse);
    }

}

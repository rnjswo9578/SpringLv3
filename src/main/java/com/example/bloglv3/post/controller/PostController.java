package com.example.bloglv3.post.controller;


import com.example.bloglv3.global.exception.RestApiException;
import com.example.bloglv3.post.dto.PostRequestDto;
import com.example.bloglv3.post.dto.PostResponseDto;
import com.example.bloglv3.post.entity.Post;
import com.example.bloglv3.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    @ResponseBody
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts(Pageable pageable) {
        return postService.getPosts(pageable); //@RequestParam 으로 들어온 page=3&size=10&sort=id,DESC을 자동으로 pageable로 만들어줌!
    }
    //Page<T>

    @ResponseBody
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @ResponseBody
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.createPost(postRequestDto, httpServletRequest);
    }

    @ResponseBody
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.updatePost(id, postRequestDto, httpServletRequest);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<RestApiException> deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return postService.deletePost(id, httpServletRequest);
    }


}

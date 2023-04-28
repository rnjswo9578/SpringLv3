package com.example.bloglv3.post.service;

import com.example.bloglv3.comment.repository.CommentRepository;
import com.example.bloglv3.global.exception.RestApiException;
import com.example.bloglv3.global.jwt.JwtUtil;
import com.example.bloglv3.post.dto.PostRequestDto;
import com.example.bloglv3.post.dto.PostResponseDto;
import com.example.bloglv3.post.entity.Post;
import com.example.bloglv3.post.repository.PostRepository;
import com.example.bloglv3.user.entity.User;
import com.example.bloglv3.user.entity.UserRoleEnum;
import com.example.bloglv3.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    public List<PostResponseDto> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable).getContent().stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long id) {
        return new PostResponseDto(getPostOrElseThrow(id));
    }

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        User user = getUserByTokenInHttpRequest(httpServletRequest);
        Post post = new Post(postRequestDto);
        post.addUser(user);

        Long id = postRepository.saveAndFlush(post).getId();
        return new PostResponseDto(getPostOrElseThrow(id));
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        User user = getUserByTokenInHttpRequest(httpServletRequest);
        Post post = getPostOrElseThrow(id);

        if (user.getUsername().equals(post.getUser().getUsername()) || user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            post.update(postRequestDto);
        }

        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseEntity<RestApiException> deletePost(Long id, HttpServletRequest httpServletRequest) {
        User user = getUserByTokenInHttpRequest(httpServletRequest);
        Post post = getPostOrElseThrow(id);

        if (user.getUsername().equals(post.getUser().getUsername()) || user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            postRepository.delete(post);
        }

        RestApiException restApiException = new RestApiException();
        restApiException.setErrorMessage("삭제 성공");
        restApiException.setHttpStatus(HttpStatus.OK);

        return new ResponseEntity<>(restApiException, HttpStatus.OK);
    }






    private User getUserByTokenInHttpRequest(HttpServletRequest httpServletRequest) {
        String token = jwtUtil.resolveToken(httpServletRequest);
        if (token == null) {
            throw new IllegalArgumentException("토큰이 없습니다!");
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다!");
        }
        Claims claims = jwtUtil.getUserInfoFromToken(token);

        return getUserOrElseThrow(claims);
    }

    private Post getPostOrElseThrow(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("post 가 없습니다!")
        );
    }

    private User getUserOrElseThrow(Claims claims) {
        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
    }

}

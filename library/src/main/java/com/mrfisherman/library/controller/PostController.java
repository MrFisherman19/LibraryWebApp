package com.mrfisherman.library.controller;

import com.mrfisherman.library.model.dto.PostDto;
import com.mrfisherman.library.model.dto.PostInsertDto;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final ModelMapper mapper;
    private final PostService postService;

    public PostController(ModelMapper mapper, PostService postService) {
        this.mapper = mapper;
        this.postService = postService;
    }

    @GetMapping("/{postId}")
    public PostDto getPost(@PathVariable Long postId) {
        return mapper.map(postService.findById(postId), PostDto.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public PostDto updatePost(@RequestBody PostInsertDto postToUpdate) {
        Post updated = postService.update(mapper.map(postToUpdate, Post.class));
        return mapper.map(updated, PostDto.class);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId) {
        postService.deleteById(postId);
    }
}

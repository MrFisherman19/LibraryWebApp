package com.mrfisherman.library.controller;

import com.mrfisherman.library.controller.hateoas.CommentResourceAssembler;
import com.mrfisherman.library.controller.hateoas.PostResourceAssembler;
import com.mrfisherman.library.model.dto.*;
import com.mrfisherman.library.model.entity.Comment;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.service.domain.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final ModelMapper mapper;
    private final PostService postService;
    private final PostResourceAssembler postResourceAssembler;
    private final CommentResourceAssembler commentResourceAssembler;
    private final PagedResourcesAssembler<Post> pagedResourcesAssembler;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PagedModel<PostDto>> getPosts(@RequestParam(required = false) Long bookId,
                                                        @PageableDefault Pageable pageable) {
        Optional<Long> bookParam = Optional.ofNullable(bookId);

        PagedModel<PostDto> page = getPostDtoModel(pageable, bookParam);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto getPost(@PathVariable Long id) {
        return postResourceAssembler.toModel(postService.findById(id));
    }

    @PatchMapping("/{id}/voteUp")
    @ResponseStatus(HttpStatus.OK)
    public PostDto voteUpPost(@PathVariable Long id) {
        return postResourceAssembler.toModel(postService.voteUp(id));
    }

    @PatchMapping("/{id}/voteDown")
    @ResponseStatus(HttpStatus.OK)
    public PostDto voteDownPost(@PathVariable Long id) {
        return postResourceAssembler.toModel(postService.voteDown(id));
    }

    @GetMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<CommentDto> getComments(@PathVariable Long id) {
        return commentResourceAssembler.toCollectionModel(postService.findCommentsByPostId(id));
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long id, @Valid @RequestBody CommentInsertDto commentDto) {
        Comment comment = mapper.map(commentDto, Comment.class);
        return mapper.map(postService.addComment(id, comment), CommentDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto createPost(@Valid @RequestBody PostInsertDto postInsertDto) {
        Post post = mapper.map(postInsertDto, Post.class);
        Post postCreated = postService.save(post);
        return mapper.map(postCreated, PostDto.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateDto postToUpdate) {
        Post updated = postService.update(id, mapper.map(postToUpdate, Post.class));
        return mapper.map(updated, PostDto.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        postService.deleteById(id);
    }

    private PagedModel<PostDto> getPostDtoModel(Pageable pageable, Optional<Long> bookParam) {
        Page<Post> postPage = bookParam.map(aLong -> postService.getByBookId(aLong, pageable))
                .orElseGet(() -> postService.getAllPosts(pageable));

        return pagedResourcesAssembler.toModel(postPage, postResourceAssembler);
    }
}

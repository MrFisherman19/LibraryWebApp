package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Comment;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.persistence.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final BookService bookService;
    private final CommentService commentService;
    private final ExceptionHelper<Post> exceptionHelper;

    @Transactional
    @Cacheable(cacheNames = "findAllPosts")
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    @Cacheable(cacheNames = "findPostById")
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(exceptionHelper.getEntityNotFoundException(id, Post.class));
    }

    @Transactional
    public Post save(Post post) {
        if (post.getBook() != null) {
            Book book = bookService.findById(post.getBook().getId());
            book.addPost(post);
        }
        return postRepository.save(post);
    }

    @Transactional
    @CacheEvict(cacheNames = "findPostById")
    public void deleteById(Long id) {
        Post post = findById(id);
        post.getBook().removePost(post);
        postRepository.delete(post);
    }

    @Transactional
    @CachePut(cacheNames = "findPostById", key = "#result.id")
    public Post update(Long postId, Post postToUpdate) {
        Post post = postRepository.getOne(postId);
        post.setTitle(postToUpdate.getTitle());
        post.setContent(postToUpdate.getContent());
        post.setUpdated(LocalDateTime.now());
        return post;
    }

    @Transactional
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentService.findByPostId(postId);
    }

    @Transactional
    public Comment addComment(Long postId, Comment comment) {
        Post post = getOne(postId);
        post.addComment(comment);
        return comment;
    }

    @Transactional
    public Page<Post> getByBookId(Long bookId, Pageable pagination) {
        return postRepository.findAllByBookId(bookId, pagination);
    }

    @Transactional
    public void voteUp(Long id) {
        postRepository.voteUp(id);
    }

    @Transactional
    public void voteDown(Long id) {
        postRepository.voteDown(id);
    }

    private Post getOne(Long id) {
        final Optional<Post> post = Optional.of(postRepository.getOne(id));
        return post.orElseThrow(exceptionHelper.getEntityNotFoundException(id, Post.class));
    }
}

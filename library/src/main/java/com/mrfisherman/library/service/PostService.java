package com.mrfisherman.library.service;

import com.mrfisherman.library.exception.NoPostFoundException;
import com.mrfisherman.library.model.dto.PostDto;
import com.mrfisherman.library.model.dto.PostInsertDto;
import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.persistence.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final BookService bookService;

    public PostService(PostRepository postRepository, BookService bookService) {
        this.postRepository = postRepository;
        this.bookService = bookService;
    }

    @Transactional
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NoPostFoundException(format("Cannot find post with given id: %d", id)));
    }

    @Transactional
    public List<Post> findAllByBookId(Long id) {
        return postRepository.findAllByBookId(id);
    }

    @Transactional
    public Post savePost(Long bookId, Post post) {
        Book book = bookService.getBook(bookId);
        book.addPost(post);
        return postRepository.save(post);
    }

    @Transactional
    public void deleteById(Long postId) {
        Post post = findById(postId);
        post.getBook().removePost(post);
        postRepository.delete(post);
    }

    @Transactional
    public Post update(Post postToUpdate) {
        Post post = postRepository.getOne(postToUpdate.getId());
        post.setTitle(postToUpdate.getTitle());
        post.setContent(postToUpdate.getContent());
        post.setUpdated(LocalDateTime.now());
        return post;
    }
}

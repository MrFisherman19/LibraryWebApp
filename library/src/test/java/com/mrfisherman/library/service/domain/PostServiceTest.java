package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.model.entity.types.BookFormat;
import com.mrfisherman.library.persistence.repository.BookRepository;
import com.mrfisherman.library.persistence.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void clearDatabase() {
        bookRepository.deleteAll();
        postRepository.deleteAll();
    }

    //new post always have to be assigned to some book
    @Test
    void should_throw_illegalArgumentException_when_book_is_null() {
        //given
        Post post = new Post("Title 1", "Content 1");
        //then
        assertThrows(IllegalArgumentException.class, () -> postService.save(post));
    }
}
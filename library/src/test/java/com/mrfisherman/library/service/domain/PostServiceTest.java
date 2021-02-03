package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.persistence.repository.BookRepository;
import com.mrfisherman.library.persistence.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clearDatabase() {
        postRepository.deleteAll();
    }
}
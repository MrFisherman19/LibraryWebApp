package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.persistence.repository.BookRepository;
import com.mrfisherman.library.persistence.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Mock
    private BookRepository mockedBookRepository;

    @Test
    void should_save_post_assigned_to_book() {
        //given
        Post post = new Post("Title 1", "Content 1");
        Book bookToReturnFromRepository = new Book();
        bookToReturnFromRepository.setId(2);
        //when
        when(mockedBookRepository.findById(any())).thenReturn(Optional.of(bookToReturnFromRepository));
        post.setBook(bookToReturnFromRepository);
        postService.save(post);
        //then
        Optional<Post> loaded = postRepository.findById(post.getId());
        assertAll(() -> {
            assertThat(loaded).isPresent();
            Post savedPost = loaded.get();
            assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
            assertThat(savedPost.getContent()).isEqualTo(post.getContent());
            assertTrue(savedPost.getComments().isEmpty());
            assertThat(savedPost.getVoteDown()).isEqualTo(0);
            assertThat(savedPost.getVoteUp()).isEqualTo(0);
        });
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
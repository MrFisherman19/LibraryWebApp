package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.model.entity.Book;
import com.mrfisherman.library.model.entity.Category;
import com.mrfisherman.library.model.entity.Comment;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.model.entity.types.BookFormat;
import com.mrfisherman.library.persistence.repository.BookRepository;
import com.mrfisherman.library.persistence.repository.CommentRepository;
import com.mrfisherman.library.persistence.repository.PostRepository;
import com.mrfisherman.library.service.domain.stubs.BookStub;
import com.mysql.cj.exceptions.AssertionFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void clearDatabase() {
        bookRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    void should_find_comment_by_id() {
        //given
        Post post = getExampleCorrectPost();
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Very good book!");
        comment.setPost(post);
        commentRepository.save(comment);
        //when
        Comment loaded = commentService.findById(comment.getId());
        //then
        assertThat(loaded).isNotNull();
        assertThat(loaded.getId()).isEqualTo(comment.getId());
        assertThat(loaded.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    void should_remove_comment_by_id() {
        //given
        Post post = getExampleCorrectPost();
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Very good book!");
        comment.setPost(post);
        commentRepository.save(comment);
        //when
        commentService.removeById(comment.getId());
        //then
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
        assertThat(postRepository.findById(post.getId())).isNotEmpty();
    }

    @Test
    void should_update_comment() {
        //given
        Post post = getExampleCorrectPost();
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Very good book!");
        comment.setPost(post);
        commentRepository.save(comment);
        //when
        Comment loadedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new AssertionFailedException("Comment is null"));
        loadedComment.setContent("It is very good book after all!");
        commentService.update(comment.getId(), loadedComment);
        //then
        assertThat(loadedComment.getContent()).isEqualTo("It is very good book after all!");
        assertThat(loadedComment.getUpdated()).isAfter(LocalDateTime.now().minusMinutes(1));
        assertThat(loadedComment.getCreated()).isEqualToIgnoringSeconds(comment.getCreated());
    }

    @Test
    void should_throw_entity_update_time_limit_exceed_exception_when_updating_after_time_limit() {
        //given
        ReflectionTestUtils.setField(commentService, "updateTimeLimitInMinutes", 10);

        Post post = getExampleCorrectPost();
        postRepository.save(post);

        var timeOfCreation = LocalDateTime.of(1970, 1,1,0, 0);

        Comment comment = new Comment();
        comment.setContent("Very good book!");
        comment.setCreated(timeOfCreation);
        comment.setPost(post);
        commentRepository.save(comment);

        Comment loadedComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new AssertionFailedException("Comment is null"));
        loadedComment.setContent("It is very good book after all!");

        //then
        assertThatThrownBy(() -> commentService.update(comment.getId(), loadedComment));
    }

    @Test
    void should_find_comment_by_post_id() {
        //given
        Post post = getExampleCorrectPost();
        postRepository.save(post);

        Comment comment = new Comment();
        comment.setContent("Very good book!");
        comment.setPost(post);
        commentRepository.save(comment);
        //when
        Comment loaded = commentService.findById(comment.getId());
        //then
        assertThat(loaded).isNotNull();
        assertThat(loaded.getCreated()).isEqualToIgnoringSeconds(comment.getCreated());
        assertThat(loaded.getUpdated()).isEqualToIgnoringSeconds(comment.getCreated());
        assertThat(loaded.getContent()).isEqualTo(comment.getContent());
    }

    @Test
    void should_throw_entity_not_found_exception_when_not_found_by_id() {
        //given
        Long commentId = 5L;
        //when
        commentRepository.deleteAll();
        //then
        assertThatThrownBy(() -> commentService.findById(commentId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Post getExampleCorrectPost() {
        BookStub bookStub = new BookStub();
        Book book = bookStub.getBook();
        bookRepository.save(book);

        Post post = new Post();
        post.setTitle("Title of post");
        post.setContent("Content of post");
        post.setCreated(LocalDateTime.now());
        post.setBook(book);

        return post;
    }
}
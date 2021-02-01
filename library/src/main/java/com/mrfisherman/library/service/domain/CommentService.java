package com.mrfisherman.library.service.domain;

import com.mrfisherman.library.exception.EntityUpdateTimeLimitExceededException;
import com.mrfisherman.library.model.entity.Comment;
import com.mrfisherman.library.persistence.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
public class CommentService {

    @Value("${comment.update_time_limit}")
    private int updateTimeLimitInMinutes;

    private final CommentRepository commentRepository;
    private final ExceptionHelper<Comment> exceptionHelper;

    @Transactional
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(exceptionHelper.getEntityNotFoundException(id, Comment.class));
    }

    @Transactional
    public void removeById(Long id) {
        commentRepository.deleteById(id);
    }

    @Transactional
    public Comment update(Long commentId, Comment commentToUpdate) {
        Comment comment = commentRepository.getOne(commentId);
        comment.setContent(commentToUpdate.getContent());
        validateUpdatingTime(comment);
        return comment;
    }

    private void validateUpdatingTime(Comment comment) {
        var maxUpdatingTime = comment.getCreated().plusMinutes(updateTimeLimitInMinutes);
        if (LocalDateTime.now().isAfter(maxUpdatingTime)) {
            throw new EntityUpdateTimeLimitExceededException(
                    format("Comment can be updated only within %d minutes!", updateTimeLimitInMinutes));
        } else {
            comment.setUpdated(LocalDateTime.now());
        }
    }

    @Transactional
    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}

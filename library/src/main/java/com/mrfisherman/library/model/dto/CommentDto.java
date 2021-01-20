package com.mrfisherman.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends RepresentationModel<CommentDto> {

    private Long id;
    private Long postId;
    private Long userId;

    private String content;

    private int voteUp;
    private int voteDown;

    private LocalDateTime created;
    private LocalDateTime updated;

}

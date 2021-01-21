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
public class PostDto extends RepresentationModel<PostDto> {

    private Long id;
    private Long bookId;

    private String title;
    private String content;

    private LocalDateTime created;
    private LocalDateTime updated;

    private int voteUp;
    private int voteDown;
}

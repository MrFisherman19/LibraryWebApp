package com.mrfisherman.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private long id;
    private long bookId;

    private String title;
    private String content;

    private LocalDateTime created;
    private LocalDateTime updated;

    private int voteUp;
    private int voteDown;

}

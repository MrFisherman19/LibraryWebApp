package com.mrfisherman.library.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostInsertDto {

    @NotNull(message = "Book id can't be null when inserting the post")
    private Long bookId;

    @NotBlank(message = "Title of post is mandatory")
    private String title;

    @NotBlank(message = "Content of post is mandatory")
    private String content;

}

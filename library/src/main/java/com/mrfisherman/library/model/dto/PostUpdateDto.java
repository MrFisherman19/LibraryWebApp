package com.mrfisherman.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDto {

    @NotBlank(message = "Title of post is mandatory")
    private String title;

    @NotBlank(message = "Content of post is mandatory")
    private String content;

}

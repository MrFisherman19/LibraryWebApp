package com.mrfisherman.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private long id;
    private String title;
    private String isbn;
    private String summary;
    private String description;
    private String type;
    private Integer publishYear;
    private Integer numberOfPages;
    private LocalDateTime created;
    private LocalDateTime updated;
    private double rating;
    private Set<CategoryDto> categories;
}

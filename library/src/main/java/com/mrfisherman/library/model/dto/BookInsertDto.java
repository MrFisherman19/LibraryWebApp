package com.mrfisherman.library.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInsertDto {

    private String title;
    private String isbn;
    private String summary;
    private String description;
    private Integer publishYear;
    private Integer numberOfPages;
    private String type;
    private Set<CategoryInsertDto> categories;

}

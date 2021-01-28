package com.mrfisherman.library.model.dto;

import com.mrfisherman.library.model.entity.Author;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateDto extends RepresentationModel<BookDto> {

    @NotBlank(message = "Title is mandatory")
    private String title;
    @NotBlank(message = "Isbn is mandatory")
    private String isbn;
    @Size(max = 300, message = "Summary have to be shorter than 300 characters")
    private String summary;
    @Size(max = 2000, message = "Description have to be shorter than 2000 characters")
    private String description;
    @Positive(message = "Year can't be negative")
    private Integer publishYear;
    @Positive(message = "Number of pages can't be negative")
    private Integer numberOfPages;
    @Enumerated(value = EnumType.STRING)
    private String type;
    private Set<CategoryInsertDto> categories;
}
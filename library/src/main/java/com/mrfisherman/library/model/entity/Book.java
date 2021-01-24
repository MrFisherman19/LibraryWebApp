package com.mrfisherman.library.model.entity;

import com.mrfisherman.library.model.entity.types.BookFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    private BookFormat type;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();

    //set 5 as default, in the middle of range
    @Min(value = 1, message = "Minimal rating is 1")
    @Max(value = 10, message = "Maximum rating is 10")
    private double rating = 5;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_categories",
            joinColumns = {@JoinColumn(name = "book_id",
                    insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "category_id",
                    insertable = false, updatable = false)})
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public void addCategories(Category category) {
        categories.add(category);
    }

    public void addPost(Post post) {
        this.posts.add(post);
        post.setBook(this);
    }

    public void removePost(Post post) {
        this.posts.remove(post);
        post.setBook(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}

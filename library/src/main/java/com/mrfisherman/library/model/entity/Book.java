package com.mrfisherman.library.model.entity;

import com.mrfisherman.library.model.entity.types.BookFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private String title;
    private String isbn;
    private String summary;
    private String description;
    private Integer publishYear;
    private Integer numberOfPages;

    @Enumerated(value = EnumType.STRING)
    private BookFormat type;
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();
    private double rating;

    //eager because is small dataset and it is not nested
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "books_categories",
            joinColumns = {@JoinColumn(name = "book_id",
                    insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "category_id",
                    insertable = false, updatable = false)})
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(mappedBy = "likedBooks")
    private Set<UserDetails> usersLiked = new HashSet<>();

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

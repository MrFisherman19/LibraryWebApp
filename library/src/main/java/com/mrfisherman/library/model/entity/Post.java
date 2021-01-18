package com.mrfisherman.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String content;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();

    private int voteUp;
    private int voteDown;

    @ManyToOne
    private Book book;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private List<Comment> comments;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package com.mrfisherman.library.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotBlank(message = "Title of post is mandatory")
    private String title;

    @NotBlank(message = "Content of post is mandatory")
    private String content;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();

    @Setter(AccessLevel.NONE)
    @PositiveOrZero
    private int voteUp;

    @Setter(AccessLevel.NONE)
    @PositiveOrZero
    private int voteDown;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    private UserDetails user;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void addVoteUp() {
        this.voteUp++;
    }

    public void addVoteDown() {
        this.voteDown++;
    }

}

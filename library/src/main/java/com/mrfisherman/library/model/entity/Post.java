package com.mrfisherman.library.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private String title;
    private String content;

    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated = LocalDateTime.now();

    private int voteUp;
    private int voteDown;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserDetails user;

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", voteUp=" + voteUp +
                ", voteDown=" + voteDown +
                ", book=" + book +
                ", comments=" + comments +
                ", user=" + user +
                '}';
    }
}

package com.mrfisherman.library.persistence.repository;

import com.mrfisherman.library.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByBookId(Long id, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    @Modifying
    @Query("UPDATE Post SET voteUp = voteUp + 1 WHERE id=:id")
    void voteUp(Long id);

    @Modifying
    @Query("UPDATE Post SET voteDown = voteDown + 1 WHERE id=:id")
    void voteDown(Long id);

}

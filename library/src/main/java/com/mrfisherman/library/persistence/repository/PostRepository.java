package com.mrfisherman.library.persistence.repository;

import com.mrfisherman.library.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByBookId(Long id);

}

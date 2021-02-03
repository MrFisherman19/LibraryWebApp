package com.mrfisherman.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrfisherman.library.model.entity.Post;
import com.mrfisherman.library.persistence.repository.PostRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clearDatabase() {
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void should_return_post_by_id() throws Exception {
        //given
        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("Test content");
        postRepository.save(post);
        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/posts/" + post.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //then
        Post resultPost = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Post.class);
        assertThat(resultPost).isNotNull();
        assertThat(resultPost.getId()).isEqualTo(post.getId());
        assertThat(resultPost.getTitle()).isEqualTo("Test title");
        assertThat(resultPost.getContent()).isEqualTo("Test content");
    }
}
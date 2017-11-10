package pick.me.PostsTask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pick.me.PostsTask.model.Post;
import pick.me.PostsTask.service.interfaces.PostService;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    private static final String BASE_URL = "/api/posts";

    private MockMvc mockMvc;

    @MockBean
    private PostController postController;

    @MockBean
    private PostService postService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(postController)
                .build();
    }

    @Test
    public void test_get_all_success() throws Exception {

        Post result = initPost();

        List<Post> allPosts = singletonList(result);
        given(postController.getAllPostst()).willReturn(allPosts);

        mockMvc.perform(get(BASE_URL)
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(result.getTitle())));
    }

    private Post initPost() {
        Post result = new Post();
        result.setTitle("Test");
        result.setContent("Test Content");
        return result;
    }

    @Test
    public void test_get_by_id_success() throws Exception {

        Post result = initPost();

        given(postController.getPostById(result.getId()))
                .willReturn(new ResponseEntity<Post>(result, HttpStatus.OK));

        mockMvc.perform(get(BASE_URL + "/" + result.getId())
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title", is(result.getTitle())));
    }

    @Test
    public void test_get_by_id_fail_404_not_found() throws Exception {

        Post result = initPost();
        postService.remove(result.getId());

        given(postService.findOne(result.getId())).willReturn(null);

        mockMvc.perform(get(BASE_URL + "{id}", 1)
                .with(user("user").password("password")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_create_post_success() throws Exception {

        Post result = new Post();
        result.setTitle("New Title");
        result.setContent("New Content");

        when(postService.exists(result)).thenReturn(false);
        doNothing().when(postService).save(result);

        mockMvc.perform(post("/api/posts")
                .with(user("user").password("password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(result)))
                .andExpect(status().isOk());

        //.andExpect(header().string("Location", containsString(BASE_URL + "/" + result.getId())));
    }

    @Test
    public void test_create_post_fail_404_not_found() throws Exception {
        Post result = new Post();
        result.setTitle("title exists");
        result.setContent("content");

        when(postService.exists(result)).thenReturn(true);

        mockMvc.perform(
                post(BASE_URL)
                        .with(user("user").password("password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(result)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_update_post_fail_404_not_found() throws Exception {
        Post result = initPost();

        when(postService.findOne(result.getId())).thenReturn(result);

        mockMvc.perform(put("/api/posts{id}", result.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(result)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_update_post_success() throws Exception {
        Post result = initPost();
        result.setId(1);

        when(postService.findOne(result.getId())).thenReturn(result);
        doNothing().when(postService).save(result);

        mockMvc.perform(put("/api/posts{id}", result.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(result)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_delete_post_success() throws Exception {

        Post result = new Post();
        result.setId(1);
        result.setTitle("Test Title");
        result.setContent("Test Content");

        when(postService.findOne(result.getId())).thenReturn(result);
        doNothing().when(postService).remove(result.getId());

        mockMvc.perform(
                delete("/api/posts/{id}", result.getId())
                        .with(user("user").password("password")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_delete_post_fail_404_not_found() throws Exception {
        Post result = initPost();
        result.setId(1);

        when(postService.findOne(result.getId())).thenReturn(null);

        mockMvc.perform(delete(BASE_URL + "{id}", result.getId()))
                .andExpect(status().isNotFound());
    }

    /*
    * converts a Java object into JSON representation
    */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
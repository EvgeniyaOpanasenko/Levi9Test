package pick.me.PostsTask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pick.me.PostsTask.model.Post;
import pick.me.PostsTask.service.interfaces.PostService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getAllPostst() {
        return postService.getAll();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "id") Long id) {
        Post newPost = postService.findOne(id);

        if (newPost == null) {
            logger.info("getting newPost with id " + id + " not found");
            return new ResponseEntity<Post>(newPost, HttpStatus.NOT_FOUND);
        }

        logger.info("getPostById id " + id);
        return new ResponseEntity<Post>(newPost, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createPost(@RequestBody Post newPost, UriComponentsBuilder ucBuilder) {


        if (postService.exists(newPost)) {
            logger.info("a post with title " + newPost.getTitle() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        postService.save(newPost);

        logger.info("Created new newPost id " + newPost.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/posts/{id}").buildAndExpand(newPost.getId()).toUri());
        ;
        return new ResponseEntity<Void>(headers, HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Post> updatePost(@PathVariable(value = "id") Long id,
                                           @Valid @RequestBody Post postDetails) {
        Post post = postService.findOne(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        post.setTitle(postDetails.getTitle());
        post.setContent(postDetails.getContent());

        postService.save(post);

        Post updatedPOst = postService.findOne(post.getId());
        return ResponseEntity.ok(updatedPOst);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> deletePost(@PathVariable(value = "id") Long id) {
        logger.info("deleting post with id " + id);
        Post targetPost = postService.findOne(id);

        if (targetPost == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        postService.remove(targetPost.getId());
        logger.info("remove targetPost " + targetPost.getId());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}
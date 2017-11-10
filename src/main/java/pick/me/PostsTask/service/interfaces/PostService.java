package pick.me.PostsTask.service.interfaces;

import pick.me.PostsTask.model.Post;

import java.util.List;


public interface PostService {
    void save(Post post);

    List<Post> getAll();

    Post findOne(Long id);

    boolean exists(Post post);

    void remove(Long id);
}

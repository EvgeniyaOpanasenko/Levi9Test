package pick.me.PostsTask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pick.me.PostsTask.model.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
}

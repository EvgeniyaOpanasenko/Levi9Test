package pick.me.PostsTask.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pick.me.PostsTask.model.UserDto;

@Repository
public interface UserDtoRepository extends CrudRepository<UserDto, Long> {

}

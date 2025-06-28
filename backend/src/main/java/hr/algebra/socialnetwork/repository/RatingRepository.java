package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.Post;
import hr.algebra.socialnetwork.model.Rating;
import hr.algebra.socialnetwork.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {
  Optional<Rating> findByPostAndUser(Post post, User user);

  @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.post.id = :postId")
  Double calculateAverageStarsForPost(@Param("postId") Long postId);
}

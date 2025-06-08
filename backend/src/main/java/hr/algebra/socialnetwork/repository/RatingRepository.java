package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {
}

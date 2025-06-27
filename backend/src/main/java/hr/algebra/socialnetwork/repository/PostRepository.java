package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.Post;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("""
            SELECT p FROM Post p
            WHERE p.id = :id
            """)
  Optional<Post> findById(@Param("id") Long id);

  @Query(
      """
            SELECT COUNT(p) > 0 FROM Post p
            WHERE p.id = :id
            """)
  boolean existsById(@Param("id") Long id);

  @Query("""
            SELECT p FROM Post p
            WHERE p.user.id = :userId
            """)
  List<Post> findAllByUserId(@Param("userId") Long userId);

  @Modifying
  @Transactional
  @Query("""
        DELETE FROM Post p
        WHERE p.id = :id
    """)
  void deleteById(@Param("id") Long id);

  @Query("""
            SELECT p FROM Post p
            """)
  List<Post> findAll();
}

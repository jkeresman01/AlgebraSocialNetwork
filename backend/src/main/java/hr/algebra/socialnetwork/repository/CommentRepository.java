package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query(
      """
            SELECT c FROM Comment c
            WHERE c.post.id = :postId
            """)
  List<Comment> findByPostId(@Param("postId") Long postId);

  @Query(
      """
            SELECT COUNT(c) > 0 FROM Comment c
            WHERE c.post.id = :postId
            """)
  boolean existsByPostId(@Param("postId") Long postId);
}

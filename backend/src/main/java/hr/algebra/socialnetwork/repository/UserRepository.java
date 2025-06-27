package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("""
            SELECT u FROM User u
            WHERE u.id = :id
            """)
  Optional<User> findById(@Param("id") Long id);

  @Query("""
            SELECT u FROM User u
            """)
  List<User> findAll();

  @Query("""
            SELECT u FROM User u
            WHERE u.email = :email
            """)
  Optional<User> findByEmail(@Param("email") String email);

  @Query(
      """
            SELECT COUNT(u) > 0 FROM User u
            WHERE u.id = :id
            """)
  boolean existsById(@Param("id") Long id);

  @Query(
      """
            SELECT COUNT(u) > 0 FROM User u
            WHERE u.email = :email
            """)
  boolean existsByEmail(@Param("email") String email);

  @Modifying
  @Transactional
  @Query(
      """
            UPDATE User u
            SET u.profileImageId = :profileImageId
            WHERE u.id = :userId
            """)
  int updateProfileImageId(
      @Param("profileImageId") String profileImageId, @Param("userId") Long userId);
}

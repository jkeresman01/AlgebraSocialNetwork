package hr.algebra.socialnetwork.repository;

import hr.algebra.socialnetwork.model.FriendRequest;
import hr.algebra.socialnetwork.model.RequestStatus;
import hr.algebra.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    @Query("""
            SELECT COUNT(fr) > 0 FROM FriendRequest fr
            WHERE fr.sender = :sender 
            AND fr.receiver = :receiver 
            AND fr.status = :status
            """)
    boolean existsBySenderAndReceiverAndStatus(
            @Param("sender") User sender,
            @Param("receiver") User receiver,
            @Param("status") RequestStatus status
    );

    @Query("""
            SELECT fr FROM FriendRequest fr
            WHERE fr.receiver = :receiver 
            AND fr.status = :status
            """)
    List<FriendRequest> findByReceiverAndStatus(
            @Param("receiver") User receiver,
            @Param("status") RequestStatus status
    );

}

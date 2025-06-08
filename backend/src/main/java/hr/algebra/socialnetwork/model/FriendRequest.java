package hr.algebra.socialnetwork.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "friend_request", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sender_id", "receiver_id"})
})
public final class FriendRequest {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
            optional = false
    )
    private User sender;

    @ManyToOne(
            optional = false
    )
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false
    )
    private RequestStatus status = RequestStatus.PENDING;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(
            nullable = false
    )
    private LocalDateTime updatedAt;
}
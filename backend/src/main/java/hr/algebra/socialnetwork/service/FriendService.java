package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.dto.FriendRequestDTO;
import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.FriendRequestDTOMapper;
import hr.algebra.socialnetwork.mapper.UserSummaryDTOMapper;
import hr.algebra.socialnetwork.model.FriendRequest;
import hr.algebra.socialnetwork.model.RequestStatus;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.repository.FriendRequestRepository;
import hr.algebra.socialnetwork.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final UserRepository userRepository;
  private final FriendRequestRepository friendRequestRepository;
  private final FriendRequestDTOMapper friendRequestDTOMapper;
  private final UserSummaryDTOMapper userSummaryDTOMapper;

  public void sendFriendRequest(String senderEmail, Long receiverId) {
    User sender =
        getUserByEmail(senderEmail, "Sender with email [%s] not found.".formatted(senderEmail));
    User receiver =
        getUserById(receiverId, "Receiver with ID [%d] not found.".formatted(receiverId));

    validateNotSelfRequest(sender.getId(), receiverId);
    validateNotAlreadyFriends(sender, receiver);
    validateNotAlreadyRequested(sender, receiver);

    FriendRequest request = createFriendRequest(sender, receiver);
    friendRequestRepository.save(request);
  }

  public void approveRequest(String receiverEmail, Long requestId) {
    User receiver =
        getUserByEmail(
            receiverEmail, "Receiver with email [%s] not found.".formatted(receiverEmail));

    FriendRequest request = getFriendRequestById(requestId);

    validateReceiverAuthorization(receiver, request);

    updateRequestStatus(request, RequestStatus.ACCEPTED);

    addAsFriends(request.getSender(), receiver);
  }

  public void declineRequest(String receiverEmail, Long requestId) {
    User receiver =
        getUserByEmail(
            receiverEmail, "Receiver with email [%s] not found.".formatted(receiverEmail));

    FriendRequest request = getFriendRequestById(requestId);

    validateReceiverAuthorization(receiver, request);

    updateRequestStatus(request, RequestStatus.REJECTED);
  }

  public void removeFriend(String requesterEmail, Long otherUserId) {
    User requester =
        getUserByEmail(
            requesterEmail,
            "User (requester) with email [%s] not found.".formatted(requesterEmail));
    User other =
        getUserById(otherUserId, "User (to remove) with ID [%d] not found.".formatted(otherUserId));

    requester.getFriends().remove(other);
    other.getFriends().remove(requester);

    userRepository.save(requester);
    userRepository.save(other);
  }

  public List<FriendRequestDTO> getPendingRequests(String receiverEmail) {
    User receiver =
        getUserByEmail(receiverEmail, "User with email [%s] not found.".formatted(receiverEmail));

    return friendRequestRepository.findByReceiverAndStatus(receiver, RequestStatus.PENDING).stream()
        .map(friendRequestDTOMapper)
        .toList();
  }

  private void validateReceiverAuthorization(User receiver, FriendRequest request) {
    if (!request.getReceiver().getId().equals(receiver.getId())) {
      throw new IllegalStateException("You are not authorized to manage this request.");
    }
  }

  private void updateRequestStatus(FriendRequest request, RequestStatus status) {
    request.setStatus(status);
    request.setUpdatedAt(LocalDateTime.now());
    friendRequestRepository.save(request);
  }

  private void addAsFriends(User sender, User receiver) {
    sender.getFriends().add(receiver);
    userRepository.save(sender);
  }

  private User getUserByEmail(String email, String errorMessage) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
  }

  private User getUserById(Long id, String errorMessage) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
  }

  private FriendRequest getFriendRequestById(Long requestId) {
    return friendRequestRepository
        .findById(requestId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Friend request with ID [%d] not found.".formatted(requestId)));
  }

  public List<UserSummaryDTO> getFriends(String email) {
    User user = getUserByEmail(email, "User with email [%s] not found.".formatted(email));
    return user.getFriends().stream().map(userSummaryDTOMapper).toList();
  }

  public List<UserSummaryDTO> getNonFriends(String requesterEmail) {
    User requester =
        getUserByEmail(
            requesterEmail,
            "User (requester) with email [%s] not found.".formatted(requesterEmail));

    Set<User> friends = requester.getFriends();

    List<User> allUsers = userRepository.findAll();
    List<User> potentialFriends =
        allUsers.stream()
            .filter(user -> !user.getId().equals(requester.getId()))
            .filter(user -> !friends.contains(user))
            .collect(Collectors.toList());

    List<FriendRequest> sentOrReceivedRequests =
        friendRequestRepository.findBySenderOrReceiverAndStatus(
            requester, requester, RequestStatus.PENDING);

    Set<Long> requestedUserIds =
        sentOrReceivedRequests.stream()
            .map(
                req -> {
                  if (req.getSender().equals(requester)) {
                    return req.getReceiver().getId();
                  } else {
                    return req.getSender().getId();
                  }
                })
            .collect(Collectors.toSet());

    potentialFriends.removeIf(user -> requestedUserIds.contains(user.getId()));

    return potentialFriends.stream().map(userSummaryDTOMapper).toList();
  }

  private void validateNotSelfRequest(Long senderId, Long receiverId) {
    if (senderId.equals(receiverId)) {
      throw new IllegalArgumentException("Cannot send a friend request to yourself.");
    }
  }

  private void validateNotAlreadyFriends(User sender, User receiver) {
    if (sender.getFriends().contains(receiver)) {
      throw new IllegalStateException("You are already friends with this user.");
    }
  }

  private void validateNotAlreadyRequested(User sender, User receiver) {
    boolean isAlreadyRequested =
        friendRequestRepository.existsBySenderAndReceiverAndStatus(
            sender, receiver, RequestStatus.PENDING);

    if (isAlreadyRequested) {
      throw new IllegalStateException("A friend request has already been sent.");
    }
  }

  private FriendRequest createFriendRequest(User sender, User receiver) {
    LocalDateTime now = LocalDateTime.now();

    return FriendRequest.builder()
        .sender(sender)
        .receiver(receiver)
        .status(RequestStatus.PENDING)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}

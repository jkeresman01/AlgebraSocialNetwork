package hr.algebra.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.mapper.UserSummaryDTOMapper;
import hr.algebra.socialnetwork.model.FriendRequest;
import hr.algebra.socialnetwork.model.RequestStatus;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.repository.FriendRequestRepository;
import hr.algebra.socialnetwork.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FriendsServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private FriendRequestRepository friendRequestRepository;
  @Mock private UserSummaryDTOMapper userSummaryDTOMapper;

  @InjectMocks private FriendService friendService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendFriendRequest_WillSave_WhenValid() {
    // GIVEN
    String senderEmail = "milica@krmpotich.com";
    Long receiverId = 2L;

    User sender = new User();
    sender.setId(1L);
    sender.setFriends(new HashSet<>());

    User receiver = new User();
    receiver.setId(receiverId);

    when(userRepository.findByEmail(senderEmail)).thenReturn(Optional.of(sender));
    when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
    when(friendRequestRepository.existsBySenderAndReceiverAndStatus(
            sender, receiver, RequestStatus.PENDING))
        .thenReturn(false);

    // WHEN
    friendService.sendFriendRequest(senderEmail, receiverId);

    // THEN
    verify(friendRequestRepository).save(any(FriendRequest.class));
  }

  @Test
  void approveRequest_WillAddUsersAsFriends_WhenAuthorized() {
    // GIVEN
    String receiverEmail = "branko@example.com";
    Long requestId = 1L;

    User receiver = new User();
    receiver.setId(2L);
    receiver.setFriends(new HashSet<>());

    User sender = new User();
    sender.setId(1L);
    sender.setFriends(new HashSet<>());

    FriendRequest request = new FriendRequest();
    request.setId(requestId);
    request.setSender(sender);
    request.setReceiver(receiver);
    request.setStatus(RequestStatus.PENDING);

    when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
    when(friendRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

    // WHEN
    friendService.approveRequest(receiverEmail, requestId);

    // THEN
    assertTrue(sender.getFriends().contains(receiver));
    verify(userRepository).save(sender);
    verify(friendRequestRepository).save(request);
  }

  @Test
  void removeFriend_WillMutuallyRemove_WhenUsersExist() {
    // GIVEN
    String requesterEmail = "milicah@example-svima.com";
    Long otherUserId = 2L;

    User requester = new User();
    requester.setId(1L);
    User other = new User();
    other.setId(otherUserId);

    requester.setFriends(new HashSet<>(Set.of(other)));
    other.setFriends(new HashSet<>(Set.of(requester)));

    when(userRepository.findByEmail(requesterEmail)).thenReturn(Optional.of(requester));
    when(userRepository.findById(otherUserId)).thenReturn(Optional.of(other));

    // WHEN
    friendService.removeFriend(requesterEmail, otherUserId);

    // THEN
    assertFalse(requester.getFriends().contains(other));
    assertFalse(other.getFriends().contains(requester));
    verify(userRepository, times(2)).save(any());
  }

  @Test
  void getFriends_WillReturnMappedFriends_WhenUserExists() {
    // GIVEN
    String email = "milicah@example.com";

    User user = new User();
    user.setId(1L);

    User friend1 = new User();
    friend1.setId(2L);
    friend1.setEmail("branko@example.com");
    friend1.setFirstName("Branko");
    friend1.setLastName("Mamic");

    user.setFriends(Set.of(friend1));

    UserSummaryDTO dto =
        new UserSummaryDTO(
            friend1.getId(), friend1.getFirstName(), friend1.getLastName(), friend1.getEmail());

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userSummaryDTOMapper.apply(friend1)).thenReturn(dto);

    // WHEN
    List<UserSummaryDTO> result = friendService.getFriends(email);

    // THEN
    assertEquals(1, result.size());
    assertEquals(dto, result.getFirst());
  }

  @Test
  void declineRequest_WillMarkAsRejected_WhenAuthorized() {
    // GIVEN
    String receiverEmail = "milica@example-svima.com";
    Long requestId = 1L;

    User receiver = new User();
    receiver.setId(2L);

    User sender = new User();
    sender.setId(1L);

    FriendRequest request = new FriendRequest();
    request.setId(requestId);
    request.setReceiver(receiver);
    request.setSender(sender);
    request.setStatus(RequestStatus.PENDING);

    when(userRepository.findByEmail(receiverEmail)).thenReturn(Optional.of(receiver));
    when(friendRequestRepository.findById(requestId)).thenReturn(Optional.of(request));

    // WHEN
    friendService.declineRequest(receiverEmail, requestId);

    // THEN
    assertEquals(RequestStatus.REJECTED, request.getStatus());
    verify(friendRequestRepository).save(request);
  }
}

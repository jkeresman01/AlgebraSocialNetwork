package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.dto.FriendRequestDTO;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.FriendRequestDTOMapper;
import hr.algebra.socialnetwork.model.FriendRequest;
import hr.algebra.socialnetwork.model.RequestStatus;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.repository.FriendRequestRepository;
import hr.algebra.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRequestDTOMapper friendRequestDTOMapper;

    public void sendFriendRequest(String senderEmail, Long receiverId) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        if (sender.getId().equals(receiverId)) {
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        if (sender.getFriends().contains(receiver)) {
            throw new IllegalStateException("Already friends");
        }

        boolean alreadyRequested = friendRequestRepository.existsBySenderAndReceiverAndStatus(
                sender, receiver, RequestStatus.PENDING);

        if (alreadyRequested) {
            throw new IllegalStateException("Friend request already sent");
        }

        FriendRequest request = new FriendRequest();
        request.setSender(sender);
        request.setReceiver(receiver);
        request.setStatus(RequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        friendRequestRepository.save(request);
    }

    public void approveRequest(String receiverEmail, Long requestId) {
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getReceiver().getId().equals(receiver.getId())) {
            throw new IllegalStateException("You are not authorized to approve this request");
        }

        request.setStatus(RequestStatus.ACCEPTED);

        User sender = request.getSender();

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        userRepository.save(receiver);
        userRepository.save(sender);
        friendRequestRepository.save(request);
    }

    public void declineRequest(String receiverEmail, Long requestId) {
        if(!userRepository.existsByEmail(receiverEmail)) {
                throw new ResourceNotFoundException("Receiver not found");
        }

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request with id [%d] not found!"));

        request.setStatus(RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }

    public void removeFriend(String requesterEmail, Long otherUserId) {
        User requester = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Requester not found"));

        User other = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Other user not found"));

        requester.getFriends().remove(other);
        other.getFriends().remove(requester);

        userRepository.save(requester);
        userRepository.save(other);
    }

    public List<FriendRequestDTO> getPendingRequests(String receiverEmail) {
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return friendRequestRepository
                .findByReceiverAndStatus(receiver, RequestStatus.PENDING)
                .stream()
                .map(friendRequestDTOMapper)
                .toList();
    }

}

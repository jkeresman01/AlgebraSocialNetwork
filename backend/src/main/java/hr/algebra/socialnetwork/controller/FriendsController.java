package hr.algebra.socialnetwork.controller;

import hr.algebra.socialnetwork.dto.FriendRequestDTO;
import hr.algebra.socialnetwork.service.FriendService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendsController {

  private final FriendService friendService;

  @PostMapping("/request/{userId}")
  public ResponseEntity<Void> sendRequest(@PathVariable Long userId, Principal principal) {
    friendService.sendFriendRequest(principal.getName(), userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/approve/{requestId}")
  public ResponseEntity<Void> approve(@PathVariable Long requestId, Principal principal) {
    friendService.approveRequest(principal.getName(), requestId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/decline/{requestId}")
  public ResponseEntity<Void> decline(@PathVariable Long requestId, Principal principal) {
    friendService.declineRequest(principal.getName(), requestId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/remove/{userId}")
  public ResponseEntity<Void> removeFriend(@PathVariable Long userId, Principal principal) {
    friendService.removeFriend(principal.getName(), userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/requests")
  public ResponseEntity<List<FriendRequestDTO>> getPendingRequests(Principal principal) {
    return ResponseEntity.ok(friendService.getPendingRequests(principal.getName()));
  }
}

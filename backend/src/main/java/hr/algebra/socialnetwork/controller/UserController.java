package hr.algebra.socialnetwork.controller;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.payload.UserUpdateRequest;
import hr.algebra.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Page<UserSummaryDTO>> getUsers(Pageable pageable) {
    return ResponseEntity.ok(userService.getAllUsers(pageable));
  }

  @DeleteMapping("{userId}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @PutMapping("/me")
  public ResponseEntity<UserDTO> updateUserById(
      @Valid @RequestBody UserUpdateRequest updateRequest, Principal principal) {
    UserDTO updatedUser = userService.updateUserByEmail(principal.getName(), updateRequest);
    return ResponseEntity.ok(updatedUser);
  }

  @PostMapping(value = "{userId}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void uploadUserProfileImage(
      @PathVariable("userId") Long userId, @RequestParam("file") MultipartFile file) {
    userService.uploadProfileImage(userId, file);
  }

  @GetMapping(value = "{userId}/profile-image", produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] getUserProfileImage(@PathVariable("userId") Long userId) {
    return userService.getProfileImage(userId);
  }
}

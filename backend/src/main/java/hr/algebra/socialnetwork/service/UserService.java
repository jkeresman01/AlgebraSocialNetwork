package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.exception.RequestValidationException;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.UserDTOMapper;
import hr.algebra.socialnetwork.mapper.UserSummaryDTOMapper;
import hr.algebra.socialnetwork.model.Gender;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.UserUpdateRequest;
import hr.algebra.socialnetwork.repository.UserRepository;
import hr.algebra.socialnetwork.s3.S3Service;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserDTOMapper userDTOMapper;
  private final UserSummaryDTOMapper userSummaryDTOMapper;
  private final PasswordEncoder passwordEncoder;
  private final S3Service s3Service;

  public Page<UserSummaryDTO> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(userSummaryDTOMapper);
  }

  public UserDTO getUserById(Long userId) {
    return userRepository
        .findById(userId)
        .map(userDTOMapper)
        .orElseThrow(
            () -> new ResourceNotFoundException("User with id [%s] not found".formatted(userId)));
  }

  public UserDTO updateUserByEmail(String email, UserUpdateRequest request) {
    User user = findUserByEmail(email);
    boolean hasChanges = applyUserUpdates(user, request);

    if (!hasChanges) {
      throw new RequestValidationException("No changes detected in the update request.");
    }

    userRepository.save(user);
    return userDTOMapper.apply(user);
  }

  private User findUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new ResourceNotFoundException("User with email [%s] not found".formatted(email)));
  }

  public void uploadProfileImage(Long userId, MultipartFile file) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    String imageId = UUID.randomUUID().toString();
    String key = "profile-images/%s/%s.jpg".formatted(userId, imageId);

    try {
      s3Service.putObject(key, file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload profile image", e);
    }

    user.setProfileImageId(imageId);
    userRepository.save(user);
  }

  public byte[] getProfileImage(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

    if (user.getProfileImageId() == null || user.getProfileImageId().isBlank()) {
      throw new ResourceNotFoundException("Profile image not set for user " + userId);
    }

    String key = "profile-images/%s/%s.jpg".formatted(userId, user.getProfileImageId());
    return s3Service.getObject(key);
  }

  private boolean applyUserUpdates(User user, UserUpdateRequest request) {
    boolean updated = false;

    updated |= updateFirstName(user, request.firstName());
    updated |= updateLastName(user, request.lastName());
    updated |= updateEmail(user, request.email());
    updated |= updateGender(user, request.gender());
    updated |= updatePassword(user, request.password());

    return updated;
  }

  private boolean updateFirstName(User user, String newFirstName) {
    if (isValidUpdate(newFirstName, user.getFirstName())) {
      user.setFirstName(newFirstName);
      return true;
    }

    return false;
  }

  private boolean updateLastName(User user, String newLastName) {
    if (isValidUpdate(newLastName, user.getLastName())) {
      user.setLastName(newLastName);
      return true;
    }

    return false;
  }

  private boolean updateEmail(User user, String newEmail) {
    if (isValidUpdate(newEmail, user.getEmail())) {
      user.setEmail(newEmail);
      return true;
    }

    return false;
  }

  private boolean updateGender(User user, Gender newGender) {
    if (newGender != null && !newGender.equals(user.getGender())) {
      user.setGender(newGender);
      return true;
    }

    return false;
  }

  private boolean updatePassword(User user, String newPassword) {
    if (newPassword != null && !newPassword.isBlank()) {
      user.setPassword(passwordEncoder.encode(newPassword));
      return true;
    }

    return false;
  }

  private boolean isValidUpdate(String newValue, String oldValue) {
    return newValue != null && !newValue.isBlank() && !newValue.equals(oldValue);
  }
}

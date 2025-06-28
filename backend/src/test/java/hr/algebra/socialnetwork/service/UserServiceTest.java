package hr.algebra.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.exception.RequestValidationException;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.UserDTOMapper;
import hr.algebra.socialnetwork.mapper.UserSummaryDTOMapper;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.UserUpdateRequest;
import hr.algebra.socialnetwork.repository.UserRepository;
import hr.algebra.socialnetwork.s3.S3Service;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserDTOMapper userDTOMapper;
  @Mock private UserSummaryDTOMapper userSummaryDTOMapper;
  @Mock private S3Service s3Service;

  @InjectMocks private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getUserById_WillReturnUserDTO_WhenUserExists() {
    // GIVEN
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    UserDTO userDTO = mock(UserDTO.class);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userDTOMapper.apply(user)).thenReturn(userDTO);

    // WHEN
    UserDTO result = userService.getUserById(userId);

    // THEN
    assertNotNull(result);
    assertEquals(userDTO, result);
  }

  @Test
  void getUserById_WillThrowException_WhenUserDoesNotExist() {
    // GIVEN
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // THEN
    assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
  }

  @Test
  void updateUserByEmail_WillUpdateAndReturnUserDTO_WhenChangesAreDetected() {
    // GIVEN
    String email = "test@example.com";
    UserUpdateRequest request =
        new UserUpdateRequest(1L, "milicahaschanged@example.com", "Milicah", "Krmpotich");

    User user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setFirstName("OldFirst");
    user.setLastName("OldLast");

    UserDTO expectedDTO =
        new UserDTO(
            user.getId(), request.email(), request.firstName(), request.lastName(), null, null);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userDTOMapper.apply(user)).thenReturn(expectedDTO);

    // WHEN
    UserDTO result = userService.updateUserByEmail(email, request);

    // THEN
    assertNotNull(result);
    assertEquals(expectedDTO, result);
    verify(userRepository).save(user);
  }

  @Test
  void updateUserByEmail_WillThrowValidationException_WhenNoChangesDetected() {
    // GIVEN
    String email = "same@example.com";
    UserUpdateRequest request = new UserUpdateRequest(1L, "same@example.com", "Same", "Name");

    User user = new User();
    user.setId(1L);
    user.setEmail("same@example.com");
    user.setFirstName("Same");
    user.setLastName("Name");

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // WHEN / THEN
    assertThrows(
        RequestValidationException.class, () -> userService.updateUserByEmail(email, request));

    verify(userRepository, never()).save(any());
  }

  @Test
  void uploadProfileImage_WillSaveImage_WhenValidUserIdAndFileProvided() throws IOException {
    // GIVEN
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    MultipartFile file = mock(MultipartFile.class);
    byte[] fileBytes = new byte[] {1, 2, 3};
    when(file.getBytes()).thenReturn(fileBytes);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // WHEN
    userService.uploadProfileImage(userId, file);

    // THEN
    verify(s3Service).putObject(startsWith("profile-images/" + userId), eq(fileBytes));
    verify(userRepository).save(user);
    assertNotNull(user.getProfileImageId());
  }

  @Test
  void uploadProfileImage_WillThrowException_WhenUserNotFound() {
    // GIVEN
    Long userId = 999L;
    MultipartFile file = mock(MultipartFile.class);
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // THEN
    assertThrows(
        ResourceNotFoundException.class, () -> userService.uploadProfileImage(userId, file));
  }

  @Test
  void uploadProfileImage_WillThrowException_WhenIOExceptionOccurs() throws IOException {
    // GIVEN
    Long userId = 1L;
    User user = new User();
    MultipartFile file = mock(MultipartFile.class);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(file.getBytes()).thenThrow(new IOException("error"));

    // THEN
    assertThrows(RuntimeException.class, () -> userService.uploadProfileImage(userId, file));
  }

  @Test
  void getProfileImage_WillReturnImageBytes_WhenImageIdIsSet() {
    // GIVEN
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    user.setProfileImageId("abc123");

    byte[] imageData = new byte[] {10, 20, 30};
    String expectedKey = "profile-images/1/abc123.jpg";

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(s3Service.getObject(expectedKey)).thenReturn(imageData);

    // WHEN
    byte[] result = userService.getProfileImage(userId);

    // THEN
    assertArrayEquals(imageData, result);
    verify(s3Service).getObject(expectedKey);
  }

  @Test
  void getProfileImage_WillThrow_WhenUserNotFound() {
    // GIVEN
    Long userId = 1L;
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    // THEN
    assertThrows(ResourceNotFoundException.class, () -> userService.getProfileImage(userId));
  }

  @Test
  void getProfileImage_WillThrow_WhenProfileImageIdIsBlank() {
    // GIVEN
    Long userId = 1L;
    User user = new User();
    user.setId(userId);
    user.setProfileImageId("  "); // Blank ID

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // THEN
    assertThrows(ResourceNotFoundException.class, () -> userService.getProfileImage(userId));
  }
}

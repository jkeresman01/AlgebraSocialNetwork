package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.exception.RequestValidationException;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.UserDTOMapper;
import hr.algebra.socialnetwork.mapper.UserSummaryDTOMapper;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.UserUpdateRequest;
import hr.algebra.socialnetwork.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper userDTOMapper;
    private final UserSummaryDTOMapper userSummaryDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public Page<UserSummaryDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userSummaryDTOMapper);

    }

    public UserDTO getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .map(userDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("User with id [%s] not found".formatted(userId)));
    }

    public UserDTO updateUserByEmail(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email [%s] not found".formatted(email)));

        boolean isUpdateValid = false;

        if (request.firstName() != null && !request.firstName().isBlank()
                && !request.firstName().equals(user.getFirstName())) {
            user.setFirstName(request.firstName());
            isUpdateValid = true;
        }

        if (request.lastName() != null && !request.lastName().isBlank()
                && !request.lastName().equals(user.getLastName())) {
            user.setLastName(request.lastName());
            isUpdateValid = true;
        }

        if (request.email() != null && !request.email().isBlank()
                && !request.email().equals(user.getEmail())) {
            user.setEmail(request.email());
            isUpdateValid = true;
        }

        if (request.gender() != null && !request.gender().equals(user.getGender())) {
            user.setGender(request.gender());
            isUpdateValid = true;
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
            isUpdateValid = true;
        }

        if (!isUpdateValid) {
            throw new RequestValidationException("No changes detected in the update request");
        }

        userRepository.save(user);
        return userDTOMapper.apply(user);
    }

//    public void uploadProfileImage(Long userId, MultipartFile file) {
//
//        if (!userRepository.existsById(userId)) {
//            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(userId));
//        }
//
//        String profileImageId = UUID.randomUUID().toString();
//
//        try {
//            s3Service.putObject(
//                    s3Buckets.getCustomer(),
//                    "profile-images/%s/%s".formatted(userId, profileImageId),
//                    file.getBytes()
//            );
//        } catch (IOException e) {
//            throw new RuntimeException("failed to upload profile image", e);
//        }
//
//        userRepository.updateProfileImageId(profileImageId, userId);
//    }
//
//    public byte[] getCustomerProfileImage(Long userId) {
//        UserDTO user = userRepository.findById(userId)
//                .map(userDTOMapper)
//                .orElseThrow(() -> new ResourceNotFoundException("customer with id [%s] not found".formatted(userId)));
//
//        if (StringUtils.isBlank(user.profileImageId())) {
//            throw new ResourceNotFoundException(
//                    "customer with id [%s] profile image not found".formatted(userId));
//        }
//
//        byte[] profileImage = s3Service.getObject(
//                s3Buckets.getUser(),
//                "profile-images/%s/%s".formatted(userId, user.profileImageId())
//        );
//        return profileImage;
//    }
}

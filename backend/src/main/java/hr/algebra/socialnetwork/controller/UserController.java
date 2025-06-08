package hr.algebra.socialnetwork.controller;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.payload.UserUpdateRequest;
import hr.algebra.socialnetwork.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

    @PutMapping("{userId}")
    public ResponseEntity<UserDTO> updateUserById(@Valid @RequestBody UserUpdateRequest updateRequest, Principal principal) {
        String email = principal.getName();
        UserDTO updatedUser = userService.updateUserByEmail(email, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long userId) {
        //TODO Implement getProfile image
        return ResponseEntity.ok().body(new byte[]{});
    }
}

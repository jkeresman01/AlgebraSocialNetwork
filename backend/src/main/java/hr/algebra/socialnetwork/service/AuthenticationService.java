package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.exception.DuplicatedResourceException;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.UserDTOMapper;
import hr.algebra.socialnetwork.model.Gender;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.AuthenticationRequest;
import hr.algebra.socialnetwork.payload.AuthenticationResponse;
import hr.algebra.socialnetwork.payload.RegistrationRequest;
import hr.algebra.socialnetwork.repository.UserRepository;
import hr.algebra.socialnetwork.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final UserDTOMapper userDTOMapper;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with email [%s] not found!".formatted(userDetails.getUsername())));

        String token = jwtUtil.generateToken(userDetails);

        return new AuthenticationResponse(token, userDTOMapper.apply(user));
    }

    public void register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicatedResourceException("Email [%s] already taken".formatted(request.getEmail()));
        }

        userRepository.save(createUser(request));
    }

    private User createUser(RegistrationRequest request) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

    }
}
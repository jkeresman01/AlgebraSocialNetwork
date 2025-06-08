package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.exception.DuplicatedResourceException;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.RegisterRequestToUserMapper;
import hr.algebra.socialnetwork.mapper.UserDTOMapper;
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
    private final RegisterRequestToUserMapper registerMapper;
    private final UserDTOMapper userDTOMapper;

    public AuthenticationResponse login(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with email [%s] not found!".formatted(userDetails.getUsername())));

        String token = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(token, userDTOMapper.apply(user));
    }

    public ResponseEntity<?> register(RegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicatedResourceException("Email already taken");
        }

        User user = registerMapper.apply(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
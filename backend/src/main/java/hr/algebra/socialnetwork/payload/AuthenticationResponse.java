package hr.algebra.socialnetwork.payload;

import hr.algebra.socialnetwork.dto.UserDTO;

public record AuthenticationResponse(String token, UserDTO user) {}

package hr.algebra.socialnetwork.dto;

import hr.algebra.socialnetwork.model.Gender;

public record UserDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    String profileImageId,
    Gender gender) {}

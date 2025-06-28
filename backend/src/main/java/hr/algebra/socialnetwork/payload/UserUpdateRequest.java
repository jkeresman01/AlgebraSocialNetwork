package hr.algebra.socialnetwork.payload;

import hr.algebra.socialnetwork.validation.AlgebraEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
    @NotNull(message = "User ID is required") Long userId,
    @NotBlank(message = "Email is required") @AlgebraEmail String email,
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName) {}

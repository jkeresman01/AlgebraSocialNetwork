package hr.algebra.socialnetwork.payload;

import hr.algebra.socialnetwork.model.Gender;
import hr.algebra.socialnetwork.validation.AlgebraEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank(message = "Email is required") @AlgebraEmail String email,
    @Size(min = 6, message = "Password must be at least 6 characters") String password,
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @NotNull(message = "Gender is required") Gender gender) {}

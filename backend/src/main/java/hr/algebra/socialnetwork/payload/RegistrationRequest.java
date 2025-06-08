package hr.algebra.socialnetwork.payload;

import hr.algebra.socialnetwork.model.Gender;
import hr.algebra.socialnetwork.validation.AlgebraEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @AlgebraEmail
        String email,

        @NotBlank(message = "Password is required")
        String password,

        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Gender is required")
        Gender gender
) {
}

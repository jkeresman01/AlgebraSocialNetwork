package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileImageId(),
                user.getGender()
        );

    }
}

package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.UserDTO;
import hr.algebra.socialnetwork.model.User;
import java.util.function.Function;
import org.springframework.stereotype.Component;

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
        user.getGender());
  }
}

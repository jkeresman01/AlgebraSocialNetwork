package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.UserSummaryDTO;
import hr.algebra.socialnetwork.model.User;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class UserSummaryDTOMapper implements Function<User, UserSummaryDTO> {
  @Override
  public UserSummaryDTO apply(User user) {
    return new UserSummaryDTO(
        user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
  }
}

package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.PostDTO;
import hr.algebra.socialnetwork.model.Post;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class PostDTOMapper implements Function<Post, PostDTO> {

  @Override
  public PostDTO apply(Post post) {
    return new PostDTO(
        post.getId(),
        post.getTitle(),
        post.getContent(),
        post.getImageId(),
        post.getAverageRating(),
        post.getUser().getId(),
        post.getUser().getFirstName() + " " + post.getUser().getLastName(),
        post.getCreatedAt());
  }
}

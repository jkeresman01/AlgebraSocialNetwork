package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.CommentDTO;
import hr.algebra.socialnetwork.model.Comment;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class CommentDTOMapper implements Function<Comment, CommentDTO> {
  @Override
  public CommentDTO apply(Comment comment) {
    return new CommentDTO(
        comment.getId(),
        comment.getContent(),
        comment.getUser().getId(),
        comment.getUser().getFirstName() + " " + comment.getUser().getLastName(),
        comment.getCreatedAt());
  }
}

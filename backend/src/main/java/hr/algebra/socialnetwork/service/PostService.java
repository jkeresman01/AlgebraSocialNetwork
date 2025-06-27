package hr.algebra.socialnetwork.service;

import hr.algebra.socialnetwork.dto.CommentDTO;
import hr.algebra.socialnetwork.dto.PostDTO;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.CommentDTOMapper;
import hr.algebra.socialnetwork.mapper.PostDTOMapper;
import hr.algebra.socialnetwork.model.Comment;
import hr.algebra.socialnetwork.model.Post;
import hr.algebra.socialnetwork.model.Rating;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.CreatePostRequest;
import hr.algebra.socialnetwork.payload.UpdatePostRequest;
import hr.algebra.socialnetwork.repository.CommentRepository;
import hr.algebra.socialnetwork.repository.PostRepository;
import hr.algebra.socialnetwork.repository.RatingRepository;
import hr.algebra.socialnetwork.repository.UserRepository;
import hr.algebra.socialnetwork.s3.S3Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final CommentRepository commentRepository;
  private final RatingRepository ratingRepository;
  private final PostDTOMapper postDTOMapper;
  private final CommentDTOMapper commentDTOMapper;
  private final S3Service s3Service;

  public Page<PostDTO> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable).map(postDTOMapper);
  }

  public PostDTO getPostById(Long id) {
    Post post = findPostById(id);
    return postDTOMapper.apply(post);
  }

  public PostDTO createPost(String email, CreatePostRequest request) {
    User user = findUserByEmail(email);
    Post post = buildPost(request, user);

    return postDTOMapper.apply(postRepository.save(post));
  }

  public List<PostDTO> getPostsByUserId(Long userId) {
    ensureUserExists(userId);

    return postRepository.findAllByUserId(userId).stream().map(postDTOMapper).toList();
  }

  public void ratePost(Long postId, String email, int stars) {
    validateStars(stars);

    Post post = findPostById(postId);
    User user = findUserByEmail(email);
    Rating rating = buildRating(post, user, stars);

    ratingRepository.save(rating);
  }

  public void commentOnPost(Long postId, String email, String content) {
    validateCommentContent(content);

    Post post = findPostById(postId);
    User user = findUserByEmail(email);
    Comment comment = buildComment(post, user, content);

    commentRepository.save(comment);
  }

  public List<CommentDTO> getCommentsFromPostWith(Long postId) {
    ensurePostExists(postId);

    return commentRepository.findByPostId(postId).stream().map(commentDTOMapper).toList();
  }

  private Post findPostById(Long postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Post with id [%d] not found.".formatted(postId)));
  }

  private void ensurePostExists(Long postId) {
    if (!postRepository.existsById(postId)) {
      throw new ResourceNotFoundException("Post with id [%d] not found.".formatted(postId));
    }
  }

  private User findUserByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () ->
                new ResourceNotFoundException("User with email [%s] not found.".formatted(email)));
  }

  private void ensureUserExists(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User with id [%d] not found.".formatted(userId));
    }
  }

  private void validateStars(int stars) {
    if (stars < 1 || stars > 5) {
      throw new IllegalArgumentException("Rating must be between 1 and 5.");
    }
  }

  private void validateCommentContent(String content) {
    if (content == null || content.isBlank()) {
      throw new IllegalArgumentException("Comment content must not be blank.");
    }
  }

  private Post buildPost(CreatePostRequest request, User user) {
    return Post.builder()
        .title(request.getTitle())
        .content(request.getTitle())
        .imageId(request.getImageId())
        .user(user)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  private Rating buildRating(Post post, User user, int stars) {
    return Rating.builder()
        .post(post)
        .user(user)
        .stars(stars)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  private Comment buildComment(Post post, User user, String content) {
    return Comment.builder()
        .post(post)
        .user(user)
        .content(content)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  public void deletePostById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Post with id [%d] not found.".formatted(id));
    }

    userRepository.deleteById(id);
  }

  public PostDTO updatePostById(Long id, UpdatePostRequest updatePostRequest) {
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("Post with id [%d] not found.".formatted(id)));

    post.setTitle(updatePostRequest.getTitle());
    post.setContent(updatePostRequest.getContent());
    post.setImageId(updatePostRequest.getImageId());
    post.setUpdatedAt(LocalDateTime.now());

    return postDTOMapper.apply(postRepository.save(post));
  }

  public void uploadPostImage(Long postId, MultipartFile file, String email) {
    Post post = findPostById(postId);
    User user = findUserByEmail(email);

    if (!post.getUser().getId().equals(user.getId())) {
      throw new IllegalArgumentException("User is not the owner of this post");
    }

    String imageId = UUID.randomUUID().toString();
    String key = "post-images/%s/%s.jpg".formatted(postId, imageId);

    try {
      s3Service.putObject(key, file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException("Failed to upload post image", e);
    }

    post.setImageId(imageId);
    post.setUpdatedAt(LocalDateTime.now());
    postRepository.save(post);
  }

  public byte[] getPostImage(Long postId) {
    Post post = findPostById(postId);

    if (post.getImageId() == null || post.getImageId().isBlank()) {
      throw new ResourceNotFoundException("Image not set for post with id [%d]".formatted(postId));
    }

    String key = "post-images/%s/%s.jpg".formatted(postId, post.getImageId());
    return s3Service.getObject(key);
  }
}

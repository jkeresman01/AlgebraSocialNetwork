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
import hr.algebra.socialnetwork.repository.CommentRepository;
import hr.algebra.socialnetwork.repository.PostRepository;
import hr.algebra.socialnetwork.repository.RatingRepository;
import hr.algebra.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;
    private final PostDTOMapper postDTOMapper;
    private final CommentDTOMapper commentDTOMapper;

    public Page<PostDTO> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(postDTOMapper);
    }

    public PostDTO getPostById(Long id) {
        return postRepository.findById(id)
                .map(postDTOMapper)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post with id [%d] not found".formatted(id)));
    }

    public PostDTO createPost(String email, CreatePostRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User with email [%s] not found".formatted(email)));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .imageId(request.imageId())
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Post saved = postRepository.save(post);

        return postDTOMapper.apply(saved);
    }

    public List<PostDTO> getPostsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with id [%d] not found".formatted(userId));
        }

        return postRepository.findAllByUserId(userId)
                .stream()
                .map(postDTOMapper)
                .toList();
    }

    public void ratePost(Long postId, String email, int stars) {
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id [%d] not found".formatted(postId)));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email [%s] not found".formatted(email)));

        Rating rating = new Rating();
        rating.setStars(stars);
        rating.setPost(post);
        rating.setUser(user);

        ratingRepository.save(rating);
    }

    public void commentOnPost(Long postId, String email, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content must not be blank");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id [%d] not found".formatted(postId)));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email [%s] not found".formatted(email)));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);

        commentRepository.save(comment);
    }

    public List<CommentDTO> getCommentsFromPostWith(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post with id [%d] not found".formatted(postId));
        }

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentDTOMapper)
                .toList();
    }

//    public String uploadPostImage(String email, MultipartFile file) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        String imageId = UUID.randomUUID().toString();
//        String path = "post-images/%s/%s".formatted(user.getId(), imageId);
//
//        try {
//            s3Service.putObject(s3Buckets.getUser(), path, file.getBytes());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload post image", e);
//        }
//
//        return imageId;
//    }
//
//    public byte[] downloadPostImage(Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new ResourceNotFoundException("Post with id [%d] not found".formatted(postId)));
//
//        if (post.getImageId() == null || post.getImageId().isBlank()) {
//            throw new ResourceNotFoundException("Post does not have an image");
//        }
//
//        String path = "post-images/%s/%s".formatted(post.getUser().getId(), post.getImageId());
//
//        return s3Service.getObject(s3Buckets.getUser(), path);
//    }
}

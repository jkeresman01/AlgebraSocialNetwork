package hr.algebra.socialnetwork.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import hr.algebra.socialnetwork.dto.PostDTO;
import hr.algebra.socialnetwork.exception.ResourceNotFoundException;
import hr.algebra.socialnetwork.mapper.CommentDTOMapper;
import hr.algebra.socialnetwork.mapper.PostDTOMapper;
import hr.algebra.socialnetwork.model.Post;
import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.CreatePostRequest;
import hr.algebra.socialnetwork.payload.UpdatePostRequest;
import hr.algebra.socialnetwork.repository.*;
import hr.algebra.socialnetwork.s3.S3Service;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

class PostServiceTest {

  @Mock private PostRepository postRepository;
  @Mock private UserRepository userRepository;
  @Mock private CommentRepository commentRepository;
  @Mock private RatingRepository ratingRepository;
  @Mock private PostDTOMapper postDTOMapper;
  @Mock private CommentDTOMapper commentDTOMapper;
  @Mock private S3Service s3Service;

  @InjectMocks private PostService postService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllPosts_WillReturnPageOfPostDTOs() {
    // GIVEN
    Post post1 = new Post();
    Post post2 = new Post();
    Page<Post> posts = new PageImpl<>(List.of(post1, post2));
    Pageable pageable = PageRequest.of(0, 10);

    PostDTO dto1 = mock(PostDTO.class);
    PostDTO dto2 = mock(PostDTO.class);

    when(postRepository.findAll(pageable)).thenReturn(posts);
    when(postDTOMapper.apply(post1)).thenReturn(dto1);
    when(postDTOMapper.apply(post2)).thenReturn(dto2);

    // WHEN
    Page<PostDTO> result = postService.getAllPosts(pageable);

    // THEN
    assertEquals(2, result.getTotalElements());
  }

  @Test
  void getPostById_WillReturnPostDTO_WhenPostExists() {
    // GIVEN
    Long postId = 1L;
    Post post = new Post();
    PostDTO postDTO = new PostDTO(postId, "title", "content", null, 0.0, null, null, null);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(postDTOMapper.apply(post)).thenReturn(postDTO);

    // WHEN
    PostDTO result = postService.getPostById(postId);

    // THEN
    assertNotNull(result);
    assertEquals(postDTO, result);
  }

  @Test
  void getPostById_WillThrowException_WhenPostNotFound() {
    // GIVEN
    Long postId = 100L;
    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // THEN
    assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(postId));
  }

  @Test
  void getPostImage_WillReturnBytes_WhenImageIsSet() {
    // GIVEN
    Long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setImageId("image123");
    byte[] expectedBytes = new byte[] {10, 20};

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(s3Service.getObject("post-images/1/image123.jpg")).thenReturn(expectedBytes);

    // WHEN
    byte[] result = postService.getPostImage(postId);

    // THEN
    assertArrayEquals(expectedBytes, result);
    verify(s3Service).getObject("post-images/1/image123.jpg");
  }

  @Test
  void getPostImage_WillThrow_WhenImageNotSetOrPostMissing() {
    // CASE 1: POST NOT FOUND
    when(postRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> postService.getPostImage(1L));

    // CASE 2: POST FOUND BUT IMAGE ID IS BLANK
    Post post = new Post();
    post.setImageId(" ");
    when(postRepository.findById(2L)).thenReturn(Optional.of(post));
    assertThrows(ResourceNotFoundException.class, () -> postService.getPostImage(2L));
  }

  @Test
  void createPost_WillSaveAndReturnPostDTO_WhenUserExists() {
    // GIVEN
    String email = "milica@example-svima.com";
    User user = new User();
    user.setId(1L);

    CreatePostRequest request = new CreatePostRequest();
    request.setTitle("Test Title");
    request.setContent("Test Content");
    request.setImageId("img123");

    Post savedPost = new Post();
    savedPost.setId(1L);

    PostDTO postDTO =
        new PostDTO(
            savedPost.getId(),
            request.getTitle(),
            request.getContent(),
            request.getImageId(),
            0.0,
            null,
            null,
            null);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(postRepository.save(any(Post.class))).thenReturn(savedPost);
    when(postDTOMapper.apply(savedPost)).thenReturn(postDTO);

    // WHEN
    PostDTO result = postService.createPost(email, request);

    // THEN
    assertNotNull(result);
    assertEquals(postDTO, result);
  }

  @Test
  void ratePost_WillCreateOrUpdateRating_AndReturnAverage() {
    // GIVEN
    Long postId = 1L;
    String email = "milicakkk@example.com";
    int stars = 4;

    User user = new User();
    Post post = new Post();
    post.setId(postId);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(ratingRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

    // WHEN
    double result = postService.ratePost(postId, email, stars);

    // THEN
    verify(ratingRepository).save(any());
    assertEquals(post.getAverageRating(), result);
  }

  @Test
  void commentOnPost_WillSaveComment_WhenValidInput() {
    // GIVEN
    Long postId = 1L;
    String email = "milicakkk@example.com";
    String content = "Nice post!";

    Post post = new Post();
    User user = new User();

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    // WHEN
    postService.commentOnPost(postId, email, content);

    // THEN
    verify(commentRepository).save(any());
  }

  @Test
  void updatePostById_WillUpdateFields_WhenPostExists() {
    // GIVEN
    Long postId = 1L;

    UpdatePostRequest request = new UpdatePostRequest();
    request.setTitle("Milica Krmpotic u Zadru");
    request.setContent("Kamen za gnjecenje zelja");
    request.setImageId("newImageId");

    Post post = new Post();
    post.setId(postId);

    Post savedPost = new Post();
    savedPost.setId(postId);
    savedPost.setTitle(request.getTitle());
    savedPost.setContent(request.getContent());
    savedPost.setImageId(request.getImageId());

    PostDTO postDTO =
        new PostDTO(
            postId,
            request.getTitle(),
            request.getContent(),
            request.getImageId(),
            0.0,
            null,
            null,
            null);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(postRepository.save(post)).thenReturn(savedPost);
    when(postDTOMapper.apply(savedPost)).thenReturn(postDTO);

    // WHEN
    PostDTO result = postService.updatePostById(postId, request);

    // THEN
    assertNotNull(result);
    assertEquals(postDTO, result);
    assertEquals("Milica Krmpotic u Zadru", savedPost.getTitle());
    assertEquals("Kamen za gnjecenje zelja", savedPost.getContent());
    assertEquals("newImageId", savedPost.getImageId());
  }

  @Test
  void uploadPostImage_WillUploadAndSetImage_WhenUserOwnsPost() throws IOException {
    // GIVEN
    Long postId = 1L;
    String email = "milica@krmpotic.com";
    MultipartFile file = mock(MultipartFile.class);
    byte[] fileBytes = new byte[] {1, 2, 3};

    User user = new User();
    user.setId(1L);
    Post post = new Post();
    post.setId(postId);
    post.setUser(user);

    when(postRepository.findById(postId)).thenReturn(Optional.of(post));
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(file.getBytes()).thenReturn(fileBytes);

    // WHEN
    postService.uploadPostImage(postId, file, email);

    // THEN
    verify(s3Service).putObject(startsWith("post-images/1/"), eq(fileBytes));
    verify(postRepository).save(post);
  }

  @Test
  void deletePostById_WillDelete_WhenExists() {
    // GIVEN
    Long postId = 1L;
    when(postRepository.existsById(postId)).thenReturn(true);

    // WHEN
    postService.deletePostById(postId);

    // THEN
    verify(postRepository).deleteById(postId);
  }

  @Test
  void deletePostById_WillThrow_WhenPostDoesNotExist() {
    // GIVEN
    Long postId = 99L;
    when(postRepository.existsById(postId)).thenReturn(false);

    // THEN
    assertThrows(ResourceNotFoundException.class, () -> postService.deletePostById(postId));
    verify(postRepository, never()).deleteById(any());
  }

  @Test
  void getFriendsPosts_WillReturnPosts_WhenFriendsExist() {
    // GIVEN
    String email = "milica@krmpotich.com";
    User friend = new User();
    friend.setId(2L);

    User user = new User();
    user.setId(1L);
    user.setFriends(Set.of(friend));

    Post friendPost = new Post();
    friendPost.setId(10L);
    friendPost.setUser(friend);

    PostDTO friendPostDTO =
        new PostDTO(10L, "Friend's Post", "Content", null, 0.0, null, null, null);

    Pageable pageable = PageRequest.of(0, 10);
    Page<Post> postPage = new PageImpl<>(List.of(friendPost));

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
    when(postRepository.findAllByUserIn(List.of(friend), pageable)).thenReturn(postPage);
    when(postDTOMapper.apply(friendPost)).thenReturn(friendPostDTO);

    // WHEN
    Page<PostDTO> result = postService.getFriendsPosts(email, pageable);

    // THEN
    assertEquals(1, result.getTotalElements());
    assertEquals(friendPostDTO, result.getContent().getFirst());
  }
}

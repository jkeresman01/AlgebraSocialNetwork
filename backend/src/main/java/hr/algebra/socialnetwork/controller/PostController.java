package hr.algebra.socialnetwork.controller;

import hr.algebra.socialnetwork.dto.CommentDTO;
import hr.algebra.socialnetwork.dto.PostDTO;
import hr.algebra.socialnetwork.payload.CreatePostRequest;
import hr.algebra.socialnetwork.payload.UpdatePostRequest;
import hr.algebra.socialnetwork.service.PostService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @GetMapping
  public ResponseEntity<Page<PostDTO>> getAllPosts(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return ResponseEntity.ok(postService.getAllPosts(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
    return ResponseEntity.ok(postService.getPostById(id));
  }

  @DeleteMapping
  public ResponseEntity<Void> deletePostById(@RequestParam Long id) {
    postService.deletePostById(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("{id}")
  public ResponseEntity<PostDTO> updatePostById(
      @RequestParam Long id, @Valid @RequestBody UpdatePostRequest updatePostRequest) {
    PostDTO postDTO = postService.updatePostById(id, updatePostRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<PostDTO> createPost(
      Principal principal, @RequestBody CreatePostRequest request) {
    return ResponseEntity.ok(postService.createPost(principal.getName(), request));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId) {
    return ResponseEntity.ok(postService.getPostsByUserId(userId));
  }

  @PostMapping("/{id}/rate")
  public ResponseEntity<Void> ratePost(
      @PathVariable Long id, Principal principal, @RequestParam int stars) {
    postService.ratePost(id, principal.getName(), stars);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/comments")
  public ResponseEntity<Void> commentOnPost(
      @PathVariable Long id, Principal principal, @RequestBody String content) {
    postService.commentOnPost(id, principal.getName(), content);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{postId}/comments")
  public ResponseEntity<List<CommentDTO>> getCommentsForPost(@PathVariable Long postId) {
    return ResponseEntity.ok(postService.getCommentsFromPostWith(postId));
  }

  @PostMapping(value = "/{postId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> uploadPostImage(
      @PathVariable Long postId, @RequestParam("file") MultipartFile file, Principal principal) {
    postService.uploadPostImage(postId, file, principal.getName());
    return ResponseEntity.ok().build();
  }

  @GetMapping(value = "/{postId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<byte[]> getPostImage(@PathVariable Long postId) {
    byte[] image = postService.getPostImage(postId);
    return ResponseEntity.ok(image);
  }
}

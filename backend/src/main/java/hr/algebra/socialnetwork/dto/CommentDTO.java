package hr.algebra.socialnetwork.dto;

public record CommentDTO(
        Long id,
        String content,
        Long userId,
        String userFullName
) {}
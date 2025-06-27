package hr.algebra.socialnetwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record CommentDTO(
    Long id,
    String content,
    Long userId,
    String userFullName,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt) {}

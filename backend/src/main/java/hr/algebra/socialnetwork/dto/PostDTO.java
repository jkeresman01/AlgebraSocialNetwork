package hr.algebra.socialnetwork.dto;

public record PostDTO(
        Long id,
        String title,
        String content,
        String imageId,
        double averageRating,
        Long userId,
        String userFullName
) {
}
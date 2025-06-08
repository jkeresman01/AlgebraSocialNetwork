package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.dto.FriendRequestDTO;
import hr.algebra.socialnetwork.model.FriendRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FriendRequestDTOMapper implements Function<FriendRequest, FriendRequestDTO> {

    @Override
    public FriendRequestDTO apply(FriendRequest request) {
        return new FriendRequestDTO(
                request.getId(),
                request.getSender().getId(),
                request.getSender().getFirstName() + " " + request.getSender().getLastName()
        );
    }
}
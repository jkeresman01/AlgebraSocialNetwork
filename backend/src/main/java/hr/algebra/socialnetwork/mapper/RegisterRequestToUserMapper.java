package hr.algebra.socialnetwork.mapper;

import hr.algebra.socialnetwork.model.User;
import hr.algebra.socialnetwork.payload.RegistrationRequest;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RegisterRequestToUserMapper implements Function<RegistrationRequest, User> {

    @Override
    public User apply(RegistrationRequest request) {
        return User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .gender(request.gender())
                .build();
    }
}

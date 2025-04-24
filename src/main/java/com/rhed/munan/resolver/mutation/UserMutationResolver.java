package com.rhed.munan.resolver.mutation;

import com.rhed.munan.model.User;
import com.rhed.munan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserMutationResolver {

    private final UserService userService;

    @MutationMapping
    public User createUser(@Argument CreateUserInput input) {
        User user = new User();
        user.setEmail(input.email());
        user.setFullName(input.fullName());
        if (input.role() != null) {
            user.setRole(User.UserRole.valueOf(input.role().name()));
        }
        return userService.createUser(user);
    }

    @MutationMapping
    public User updateUser(@Argument UUID id, @Argument UpdateUserInput input) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (input.email() != null) {
            user.setEmail(input.email());
        }
        if (input.fullName() != null) {
            user.setFullName(input.fullName());
        }
        if (input.role() != null) {
            user.setRole(User.UserRole.valueOf(input.role().name()));
        }

        return userService.updateUser(user);
    }

    @MutationMapping
    public boolean deleteUser(@Argument UUID id) {
        userService.deleteUser(id);
        return true;
    }

    // Input classes
    record CreateUserInput(String email, String fullName, UserRole role) {}
    record UpdateUserInput(String email, String fullName, UserRole role) {}
    enum UserRole { USER, OWNER }
}
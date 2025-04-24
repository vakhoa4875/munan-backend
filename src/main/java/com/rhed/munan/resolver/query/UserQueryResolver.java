package com.rhed.munan.resolver.query;

import com.rhed.munan.model.User;
import com.rhed.munan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UserQueryResolver {

    private final UserService userService;

    @QueryMapping
    public User userById(@Argument UUID id) {
        return userService.getUserById(id).orElse(null);
    }

    @QueryMapping
    public User userByEmail(@Argument String email) {
        return userService.getUserByEmail(email).orElse(null);
    }

    @QueryMapping
    public List<User> allUsers() {
        return userService.getAllUsers();
    }
}
package com.rhed.munan.service;

import com.rhed.munan.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    void deleteUser(UUID id);

    boolean existsByEmail(String email);

    boolean verifyUserPhone(UUID userId, String phoneNumber);
}
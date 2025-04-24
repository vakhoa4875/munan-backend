package com.rhed.munan.service.impl;

import com.rhed.munan.exception.DuplicateResourceException;
import com.rhed.munan.exception.ExceptionUtils;
import com.rhed.munan.model.User;
import com.rhed.munan.repository.UserRepository;
import com.rhed.munan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw ExceptionUtils.duplicate("User", "email", user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // Verify user exists
        userRepository.findById(user.getId())
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", user.getId()));

        // Check if email is being changed and if it's already taken
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    if (!existingUser.getId().equals(user.getId())) {
                        throw ExceptionUtils.duplicate("User", "email", user.getEmail());
                    }
                });

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw ExceptionUtils.notFound("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean verifyUserPhone(UUID userId, String phoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ExceptionUtils.notFound("User", "id", userId));

        user.setPhoneVerified(true);
        userRepository.save(user);
        return true;
    }
}
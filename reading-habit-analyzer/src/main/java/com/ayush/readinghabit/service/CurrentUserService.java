package com.ayush.readinghabit.service;

import com.ayush.readinghabit.entity.User;
import com.ayush.readinghabit.exception.ResourceNotFoundException;
import com.ayush.readinghabit.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );
    }

    public void validateUserAccess(
            Long requestedUserId,
            Long resourceUserId) {

        if (!requestedUserId.equals(resourceUserId)) {
            throw new SecurityException(
                    "You do not have permission to access this resource"
            );
        }
    }
}

package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.jwtauth.entity.User;
import dev.triacontakaihenagon.jwtauth.exception.UserNotFoundException;
import dev.triacontakaihenagon.jwtauth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) userRepository.deleteById(id);
        else throw new UserNotFoundException("User with " + id + " not found ");
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with "+ id +" not found "));
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setRole(updatedUser.getRole());
        return userRepository.save(existingUser);
    }

    public Optional<User> getCurrentUser(String username) {
        return userRepository.findByUserName(username);
    }
}

package dev.triacontakaihenagon.tasktrackerapi.service;

import dev.triacontakaihenagon.tasktrackerapi.dto.UserFilter;
import dev.triacontakaihenagon.tasktrackerapi.dto.UserRequest;
import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import dev.triacontakaihenagon.tasktrackerapi.exception.UserNotFoundException;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserRepository;
import dev.triacontakaihenagon.tasktrackerapi.repository.UserSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public User createUser(UserRequest request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with " + id + " not found "));
        existingUser.setUserName(request.getUserName());
        existingUser.setRole(request.getRole());
        return userRepository.save(existingUser);
    }
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) userRepository.deleteById(id);
        else throw new UserNotFoundException("User with " + id + " not found ");
    }

    public Optional<User> getCurrentUser(String username) {
        return userRepository.findByUserName(username);
    }
    public Page<User> search(UserFilter f, Pageable pageable) {
        Specification<User> spec = Specification.allOf(
                UserSpecs.hasRole(f.role()),
                UserSpecs.nameContains(f.name()));
        return userRepository.findAll(spec, pageable);
    }
}

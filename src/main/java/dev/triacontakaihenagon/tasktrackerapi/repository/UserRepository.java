package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
}

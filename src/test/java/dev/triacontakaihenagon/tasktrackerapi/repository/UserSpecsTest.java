package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.Role;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserSpecsTest {

    @Autowired UserRepository userRepository;

    private void save(String name, Role role) {
        User u = new User();
        u.setUserName(name);
        u.setRole(role);
        u.setPassword("hashed");
        userRepository.save(u);
    }

    @Test
    void hasRole_filtersByRole() {
        save("alice", Role.ADMIN);
        save("bob", Role.USER);

        List<User> result = userRepository.findAll(
                Specification.allOf(UserSpecs.hasRole(Role.ADMIN)));

        assertThat(result).extracting(User::getUserName).containsExactly("alice");
    }

    @Test
    void hasRole_null_returnsEverything() {
        save("alice", Role.ADMIN);
        save("bob", Role.USER);

        List<User> result = userRepository.findAll(
                Specification.allOf(UserSpecs.hasRole(null)));

        assertThat(result).hasSize(2);
    }

    @Test
    void combinedRoleAndNameFilter_appliesBoth() {
        save("alice", Role.ADMIN);
        save("albert", Role.USER);
        save("bob", Role.ADMIN);

        List<User> result = userRepository.findAll(Specification.allOf(
                UserSpecs.hasRole(Role.ADMIN),
                UserSpecs.nameContains("al")));

        assertThat(result).extracting(User::getUserName).containsExactly("alice");
    }
}
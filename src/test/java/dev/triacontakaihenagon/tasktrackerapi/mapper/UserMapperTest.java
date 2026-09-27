package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.UserResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    void toResponse_mapsTaskIds() {
        Task task1 = new Task(); task1.setId(1L);
        Task task2 = new Task(); task2.setId(2L);

        User user = new User();
        user.setId(9L);
        user.setUserName("alice");
        user.getTasks().add(task1);
        user.getTasks().add(task2);

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getTaskIds()).containsExactly(1L, 2L);
    }
}
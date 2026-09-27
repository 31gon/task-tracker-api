package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private final TaskMapper mapper = new TaskMapperImpl();

    @Test
    void toResponse_mapsRelationsToIds() {
        User user = new User(); user.setId(1L);
        Category category = new Category(); category.setId(2L);
        Comment comment = new Comment(); comment.setId(3L);
        Label label = new Label(); label.setId(4L);

        Task task = new Task();
        task.setId(10L);
        task.setTitle("Write tests");
        task.setUser(user);
        task.setCategory(category);
        task.getComments().add(comment);
        task.getLabels().add(label);

        TaskResponse response = mapper.toResponse(task);

        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getCategoryId()).isEqualTo(2L);
        assertThat(response.getCommentsId()).containsExactly(3L);
        assertThat(response.getLabelsId()).containsExactly(4L);
    }

    @Test
    void toResponse_handlesNullCategory() {
        User user = new User(); user.setId(1L);
        Task task = new Task();
        task.setUser(user);
        task.setCategory(null);

        TaskResponse response = mapper.toResponse(task);

        assertThat(response.getCategoryId()).isNull();
    }

    @Test
    void toResponseList_mapsEachTask() {
        User user = new User(); user.setId(1L);
        Task task1 = new Task(); task1.setId(1L); task1.setUser(user);
        Task task2 = new Task(); task2.setId(2L); task2.setUser(user);

        List<TaskResponse> result = mapper.toResponseList(List.of(task1, task2));

        assertThat(result).extracting(TaskResponse::getId).containsExactly(1L, 2L);
    }
}
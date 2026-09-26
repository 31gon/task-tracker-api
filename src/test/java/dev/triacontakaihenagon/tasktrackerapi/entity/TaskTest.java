package dev.triacontakaihenagon.tasktrackerapi.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void newTask_hasDefaultStatusAndPriority() {
        Task task = new Task();

        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
    }

    @Test
    void newTask_hasEmptyCollectionsNotNull() {
        Task task = new Task();

        assertThat(task.getComments()).isEmpty();
        assertThat(task.getLabels()).isEmpty();
    }

    @Test
    void onCreate_setsCreatedAt() {
        Task task = new Task();
        assertThat(task.getCreatedAt()).isNull();

        task.onCreate();

        assertThat(task.getCreatedAt()).isNotNull();
    }
}
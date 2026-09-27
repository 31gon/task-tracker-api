package dev.triacontakaihenagon.tasktrackerapi.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

    @Test
    void isOverdue_whenDueDateInPastAndNotDone_returnsTrue() {
        Task task = new Task();
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(TaskStatus.TODO);

        assertThat(task.isOverdue()).isTrue();
    }

    @Test
    void isOverdue_whenDueDateInPastAndDone_returnsFalse() {
        Task task = new Task();
        task.setDueDate(LocalDateTime.now().minusDays(1));
        task.setStatus(TaskStatus.DONE);

        assertThat(task.isOverdue()).isFalse();
    }

    @Test
    void isOverdue_whenDueDateInFuture_returnsFalse() {
        Task task = new Task();
        task.setDueDate(LocalDateTime.now().plusDays(1));
        task.setStatus(TaskStatus.TODO);

        assertThat(task.isOverdue()).isFalse();
    }

    @Test
    void isOverdue_whenDueDateNull_returnsFalse() {
        Task task = new Task();
        task.setStatus(TaskStatus.TODO);

        assertThat(task.isOverdue()).isFalse();
    }
}
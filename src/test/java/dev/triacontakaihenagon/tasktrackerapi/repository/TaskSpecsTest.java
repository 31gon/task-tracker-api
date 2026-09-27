package dev.triacontakaihenagon.tasktrackerapi.repository;

import dev.triacontakaihenagon.tasktrackerapi.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskSpecsTest {

    @Autowired TaskRepository taskRepository;
    @Autowired
    TestEntityManager entityManager;

    private User user;
    private Category workCategory;
    private Category homeCategory;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserName("alice");
        user.setPassword("hashed");
        user.setRole(Role.USER);
        entityManager.persist(user);

        workCategory = new Category();
        workCategory.setName("Work");
        entityManager.persist(workCategory);

        homeCategory = new Category();
        homeCategory.setName("Home");
        entityManager.persist(homeCategory);
    }

    private Task save(String title, TaskStatus status, TaskPriority priority,
                       Category category, LocalDateTime createdAt) {
        Task task = new Task();
        task.setTitle(title);
        task.setUser(user);
        task.setStatus(status);
        task.setPriority(priority);
        task.setCategory(category);
        entityManager.persist(task);
        // createdAt is set by @PrePersist to "now" — override afterward for date-range tests
        if (createdAt != null) {
            task.setCreatedAt(createdAt);
            entityManager.merge(task);
        }
        entityManager.flush();
        return task;
    }

    private Task save(String title, TaskStatus status, TaskPriority priority,
                      Category category, LocalDateTime createdAt, LocalDateTime dueDate) {
        Task task = save(title, status, priority, category, createdAt);
        task.setDueDate(dueDate);
        entityManager.merge(task);
        entityManager.flush();
        return task;
    }

    @Test
    void hasStatus_filtersExactMatch() {
        save("A", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);
        save("B", TaskStatus.DONE, TaskPriority.LOW, workCategory, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.hasStatus(TaskStatus.DONE)));

        assertThat(result).extracting(Task::getTitle).containsExactly("B");
    }

    @Test
    void hasStatus_null_returnsEverything() {
        save("A", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);
        save("B", TaskStatus.DONE, TaskPriority.LOW, workCategory, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.hasStatus(null)));

        assertThat(result).hasSize(2);
    }

    @Test
    void hasPriority_filtersExactMatch() {
        save("A", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);
        save("B", TaskStatus.TODO, TaskPriority.HIGH, workCategory, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.hasPriority(TaskPriority.HIGH)));

        assertThat(result).extracting(Task::getTitle).containsExactly("B");
    }

    @Test
    void inCategory_filtersByCategoryId() {
        save("A", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);
        save("B", TaskStatus.TODO, TaskPriority.LOW, homeCategory, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.inCategory(workCategory.getId())));

        assertThat(result).extracting(Task::getTitle).containsExactly("A");
    }

    @Test
    void titleContains_treatsPercentAsLiteralCharacter() {
        save("100%done", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);
        save("100xdone", TaskStatus.TODO, TaskPriority.LOW, workCategory, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.titleContains("100%")));

        assertThat(result).extracting(Task::getTitle).containsExactly("100%done");
    }

    @Test
    void createdFrom_isInclusiveOfStartOfDay() {
        LocalDate day = LocalDate.of(2026, 1, 15);
        save("exactly midnight", TaskStatus.TODO, TaskPriority.LOW, workCategory, day.atStartOfDay());
        save("one second before", TaskStatus.TODO, TaskPriority.LOW, workCategory,
                day.atStartOfDay().minusSeconds(1));

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.createdFrom(day)));

        assertThat(result).extracting(Task::getTitle).containsExactly("exactly midnight");
    }

    @Test
    void createdTo_isExclusiveOfNextDay() {
        LocalDate day = LocalDate.of(2026, 1, 15);
        save("last moment of day", TaskStatus.TODO, TaskPriority.LOW, workCategory,
                day.plusDays(1).atStartOfDay().minusSeconds(1));
        save("start of next day", TaskStatus.TODO, TaskPriority.LOW, workCategory,
                day.plusDays(1).atStartOfDay());

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.createdTo(day)));

        assertThat(result).extracting(Task::getTitle).containsExactly("last moment of day");
    }

    @Test
    void allFilters_combineWithAnd() {
        save("match", TaskStatus.TODO, TaskPriority.HIGH, workCategory, null);
        save("wrong status", TaskStatus.DONE, TaskPriority.HIGH, workCategory, null);
        save("wrong category", TaskStatus.TODO, TaskPriority.HIGH, homeCategory, null);

        List<Task> result = taskRepository.findAll(Specification.allOf(
                TaskSpecs.hasStatus(TaskStatus.TODO),
                TaskSpecs.hasPriority(TaskPriority.HIGH),
                TaskSpecs.inCategory(workCategory.getId())));

        assertThat(result).extracting(Task::getTitle).containsExactly("match");
    }

    @Test
    void overdue_matchesPastDueDateAndNotDone() {
        save("late todo", TaskStatus.TODO, TaskPriority.LOW, workCategory, null,
                LocalDateTime.now().minusDays(1));
        save("late but done", TaskStatus.DONE, TaskPriority.LOW, workCategory, null,
                LocalDateTime.now().minusDays(1));
        save("future", TaskStatus.TODO, TaskPriority.LOW, workCategory, null,
                LocalDateTime.now().plusDays(1));
        save("no due date", TaskStatus.TODO, TaskPriority.LOW, workCategory, null, null);

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.overdue()));

        assertThat(result).extracting(Task::getTitle).containsExactly("late todo");
    }

    @Test
    void ownedBy_filtersByUsername() {
        User bob = new User();
        bob.setUserName("bob");
        bob.setPassword("hashed");
        bob.setRole(Role.USER);
        entityManager.persist(bob);

        Task aliceTask = save("alice's", TaskStatus.TODO, TaskPriority.LOW, workCategory, null,
                LocalDateTime.now().minusDays(1));
        Task bobTask = new Task();
        bobTask.setTitle("bob's");
        bobTask.setUser(bob);
        bobTask.setStatus(TaskStatus.TODO);
        bobTask.setPriority(TaskPriority.LOW);
        bobTask.setCategory(workCategory);
        bobTask.setDueDate(LocalDateTime.now().minusDays(1));
        entityManager.persist(bobTask);
        entityManager.flush();

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.overdue(), TaskSpecs.ownedBy("alice")));

        assertThat(result).extracting(Task::getTitle).containsExactly(aliceTask.getTitle());
    }

    @Test
    void ownedBy_null_returnsEverything() {
        save("A", TaskStatus.TODO, TaskPriority.LOW, workCategory, null, LocalDateTime.now().minusDays(1));

        List<Task> result = taskRepository.findAll(
                Specification.allOf(TaskSpecs.overdue(), TaskSpecs.ownedBy(null)));

        assertThat(result).hasSize(1);
    }
}
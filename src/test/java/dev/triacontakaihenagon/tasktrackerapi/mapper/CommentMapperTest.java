package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.CommentResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Comment;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private final CommentMapper mapper = new CommentMapperImpl();

    @Test
    void toResponse_mapsAuthorAndTaskIds() {
        User author = new User(); author.setId(5L);
        Task task = new Task(); task.setId(7L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("nice");
        comment.setAuthor(author);
        comment.setTask(task);

        CommentResponse response = mapper.toResponse(comment);

        assertThat(response.getAuthorId()).isEqualTo(5L);
        assertThat(response.getTaskId()).isEqualTo(7L);
        assertThat(response.getContent()).isEqualTo("nice");
    }
}
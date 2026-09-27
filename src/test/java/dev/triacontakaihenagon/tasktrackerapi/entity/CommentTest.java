package dev.triacontakaihenagon.tasktrackerapi.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void onCreate_setsCreatedAt() {
        Comment comment = new Comment();
        assertThat(comment.getCreatedAt()).isNull();

        comment.onCreate();

        assertThat(comment.getCreatedAt()).isNotNull();
    }
}
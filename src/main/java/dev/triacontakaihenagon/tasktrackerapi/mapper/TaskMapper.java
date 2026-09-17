package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.TaskResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Comment;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "comments", target = "commentsId")
    @Mapping(source = "labels", target = "labelsId")
    TaskResponse toResponse(Task task);

    List<TaskResponse> toResponseList(List<Task> tasks);

    default Long commentToId(Comment comment) {
        return comment.getId();
    }

    default Long labelToId(Label label) {
        return label.getId();
    }
}
package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.UserResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Task;
import dev.triacontakaihenagon.tasktrackerapi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "tasks", target = "taskIds")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    default Long taskToId(Task task) {
        return task.getId();
    }
}
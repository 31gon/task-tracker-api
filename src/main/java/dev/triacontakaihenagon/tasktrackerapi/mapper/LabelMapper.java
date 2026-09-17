package dev.triacontakaihenagon.tasktrackerapi.mapper;

import dev.triacontakaihenagon.tasktrackerapi.dto.LabelResponse;
import dev.triacontakaihenagon.tasktrackerapi.entity.Label;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {
    LabelResponse toResponse(Label category);
    List<LabelResponse> toResponseList(List<Label> categories);
}
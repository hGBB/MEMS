package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.UserGroupDTO;
import de.unipa.hams.hamer.domain.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class GroupMapper {

  @NotNull
  public abstract UserGroup toEntity(UserGroupDTO userGroup);

  @NotNull
  public abstract UserGroupDTO toDto(UserGroup UserGroupDTO);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "users", ignore = true)
  @Mapping(target = "nodes", ignore = true)
  public abstract void merge(UserGroup source, @MappingTarget UserGroup target);
}

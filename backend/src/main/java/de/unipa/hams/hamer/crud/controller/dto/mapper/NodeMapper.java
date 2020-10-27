package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.NodeDTO;
import de.unipa.hams.hamer.domain.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class NodeMapper {

  public abstract NodeDTO entityToDTO(Node node);

  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "groups", ignore = true)
  public abstract Node dtoToEntity(NodeDTO nodeDTO);

  @Mapping(target = "uuid", ignore = true)
  @Mapping(target = "groups", ignore = true)
  public abstract void merge(Node source, @MappingTarget Node target);
}

package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.NodePinDTO;
import de.unipa.hams.hamer.crud.domain.NodePin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class NodePinMapper {
  public abstract NodePinDTO toDto(NodePin nodePin);
}

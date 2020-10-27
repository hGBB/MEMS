package de.unipa.hams.hamer.reactive.controller.dto;

import de.unipa.hams.hamer.reactive.domain.DashboardNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class DashboardNodeMapper {
  @Mapping(target = "node.id", source = "id")
  public abstract DashboardNode toDomain(DashboardNodeDTO dashboardNodeDTO);

  DashboardNode.Mode toMode(String mode) {
    return DashboardNode.Mode.valueOf(mode.toUpperCase());
  }
}

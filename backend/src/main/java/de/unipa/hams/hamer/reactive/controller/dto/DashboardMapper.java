package de.unipa.hams.hamer.reactive.controller.dto;

import de.unipa.hams.hamer.reactive.domain.DashboardConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DashboardNodeMapper.class)
public abstract class DashboardMapper {

  @Mapping(target = "uuid", expression = "java( java.util.UUID.randomUUID().toString() )")
  @Mapping(target = "nodes", source = "dashboardNodes")
  public abstract DashboardConfiguration toDomain(DashboardDTO dashboardDTO);

}

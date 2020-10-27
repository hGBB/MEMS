package de.unipa.hams.hamer.reactive.controller.dto;

import de.unipa.hams.hamer.reactive.domain.DashboardValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DashboardNodeValueMapper.class})
public abstract class DashboardValueMapper {

  @Mapping(target = "nodeRecords", source = "nodes")
  public abstract DashboardRecordDTO toDto(DashboardValue domain);

}

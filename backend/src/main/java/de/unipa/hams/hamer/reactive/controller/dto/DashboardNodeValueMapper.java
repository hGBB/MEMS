package de.unipa.hams.hamer.reactive.controller.dto;

import de.unipa.hams.hamer.crud.controller.dto.mapper.RecordMapper;
import de.unipa.hams.hamer.reactive.domain.DashboardNodeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RecordMapper.class})
public abstract class DashboardNodeValueMapper {

  @Mapping(target = "nodeId", source = "node.id")
  public abstract DashboardNodeRecordDTO toDto(DashboardNodeValue domain);
}

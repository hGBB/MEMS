package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.reactive.controller.dto.RecordDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = KpiMapper.class)
public abstract class RecordMapper {

  public abstract Record toEntity(RecordDTO recordDTO);

  public abstract RecordDTO toDTO(Record record);
}

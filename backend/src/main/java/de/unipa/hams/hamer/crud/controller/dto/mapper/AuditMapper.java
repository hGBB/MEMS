package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.AuditDTO;
import de.unipa.hams.hamer.domain.Audit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AuditMapper {

  @Mapping(source = "timestamp", target = "timestamp", dateFormat = "dd-MM-yyyy HH:mm:ss")
  public abstract AuditDTO toDTO(Audit audit);


}

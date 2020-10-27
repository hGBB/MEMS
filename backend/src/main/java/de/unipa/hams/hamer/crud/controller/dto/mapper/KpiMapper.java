package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.KpiDTO;
import de.unipa.hams.hamer.domain.Kpi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class KpiMapper {

  @Mapping(target = "unit", source = "unit.name")
  public abstract KpiDTO toDto(Kpi kpi);

  public abstract Kpi toEntity(KpiDTO kpiDTO);

  Kpi.Unit toEnum(String string) {
    for (Kpi.Unit u : Kpi.Unit.values()) {
      if (u.getName().equals(string)) {
        return u;
      }
    }
    return null;
  }
}

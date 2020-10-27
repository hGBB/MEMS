package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.AlertRuleDTO;
import de.unipa.hams.hamer.domain.AlertRule;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapping Data Transfer Objects using mapstruct
 */
@Mapper(componentModel = "spring")
public abstract class AlertMapper {

  /**
   * Transform DTOs to Entities
   *
   * @param alertRule Alert Rule DTO
   * @return Alert Rule Entity
   */
  @NotNull
  @Mapping(target = "configuredBy", ignore = true)
  public abstract AlertRule toEntity(AlertRuleDTO alertRule);

  /**
   * Transform Entities to DTO
   *
   * @param alertRuleDTO Alert Rule Entity
   * @return Alert Rule DTO
   */
  @NotNull
  @Mapping(target = "kpis", ignore = true)
  @Mapping(target = "nodes", ignore = true)
  public abstract AlertRuleDTO toDto(AlertRule alertRuleDTO);

  AlertRule.Method toEnum(String string) {
    for (AlertRule.Method m : AlertRule.Method.values()) {
      if (m.toString().equals(string)) {
        return m;
      }
    }
    return null;
  }

}

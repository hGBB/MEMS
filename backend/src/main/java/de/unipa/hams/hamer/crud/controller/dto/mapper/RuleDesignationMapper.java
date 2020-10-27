package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.RuleDesignationDTO;
import de.unipa.hams.hamer.domain.RuleDesignation;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RuleDesignationMapper {

  @NotNull
  @Mapping(target = "id.node.id", source = "nodeId")
  @Mapping(target = "id.rule.id", source = "ruleId")
  @Mapping(target = "id.kpi.id", source = "kpiId")
  public abstract RuleDesignation toEntity(RuleDesignationDTO designationDTO);

  @NotNull
  @Mapping(source = "id.node.id", target = "nodeId")
  @Mapping(source = "id.rule.id", target = "ruleId")
  @Mapping(source = "id.kpi.id", target = "kpiId")
  public abstract RuleDesignationDTO toDTO(RuleDesignation ruleDesignation);

}

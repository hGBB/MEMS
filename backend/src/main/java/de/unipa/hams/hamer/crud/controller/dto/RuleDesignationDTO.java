package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleDesignationDTO {
  private long nodeId;
  private long ruleId;
  private long kpiId;
  private boolean lowerOrEquals;
  private double threshold;
}

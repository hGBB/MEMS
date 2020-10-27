package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AlertRuleDTO {

  private long id;
  private String name;
  private String method;
  private int timespan;
  private int sleep;
  private List<NodeDTO> nodes;
  private List<AlertKpiDTO> kpis;
}

package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;

@Data
public class AlertKpiDTO {
  private double threshold;
  private boolean leq;
  private KpiDTO kpiDTO;
}

package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DashboardConfigDTO {
  private long nodeId;
  private String kpiName;
  private boolean live;
  private Date start;
  private Date stop;
  private double resolution;

}

package de.unipa.hams.hamer.monitoring.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class RawRecordsDTO {
  private String uuid;
  private String node_name;
  private int interval;
  private List<KpiValueDTO> kpis;

  @Data
  public static class KpiValueDTO {
    String name;
    Double value;
  }
}

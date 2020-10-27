package de.unipa.hams.hamer.reactive.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardRecordDTO {
  private String uuid;
  private String user;
  private List<DashboardNodeRecordDTO> nodeRecords;
}

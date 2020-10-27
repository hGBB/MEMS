package de.unipa.hams.hamer.reactive.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardNodeRecordDTO {
  private long nodeId;
  private List<RecordDTO> records;
}

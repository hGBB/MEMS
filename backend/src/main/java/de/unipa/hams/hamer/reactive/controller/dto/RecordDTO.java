package de.unipa.hams.hamer.reactive.controller.dto;

import de.unipa.hams.hamer.crud.controller.dto.KpiDTO;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDTO {
  private long id;
  private Date timestamp;
  private KpiDTO type;
  private Double value;
  private boolean estimated;
}

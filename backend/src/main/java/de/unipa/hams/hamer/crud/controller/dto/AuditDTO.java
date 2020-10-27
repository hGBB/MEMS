package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;

@Data
public class AuditDTO {
  private long id;
  private String timestamp;
  private String actionPerformed;
  private String performedByName;
  private String targetName;
}

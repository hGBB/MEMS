package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;

@Data
public class NotificationDTO {
  private long id;
  private String timestamp;
  private String text;
  private long alertRuleId;
  private String alertRuleName;
}

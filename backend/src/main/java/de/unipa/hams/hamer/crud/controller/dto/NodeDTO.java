package de.unipa.hams.hamer.crud.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO {
  private long id;
  private String name;
  private int sendInterval;
  private boolean estimated;
  private boolean registered;
}


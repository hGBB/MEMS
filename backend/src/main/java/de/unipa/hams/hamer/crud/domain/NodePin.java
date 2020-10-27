package de.unipa.hams.hamer.crud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "value")
@AllArgsConstructor
public class NodePin {
  private String value;
  private int validity;
}

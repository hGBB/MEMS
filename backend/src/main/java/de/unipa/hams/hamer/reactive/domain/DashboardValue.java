package de.unipa.hams.hamer.reactive.domain;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class DashboardValue {
  private final String uuid;
  private final String user;
  private List<DashboardNodeValue> nodes;

  public DashboardValue(String uuid, String user) {
    this.uuid = uuid;
    this.user = user;
    this.nodes = new LinkedList<>();
  }
}

package de.unipa.hams.hamer.reactive.domain;

import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.Record;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class DashboardNodeValue {
  private final Node node;
  private List<Record> records;

  public DashboardNodeValue(Node node) {
    this.node = node;
    this.records = new LinkedList<>();
  }
}

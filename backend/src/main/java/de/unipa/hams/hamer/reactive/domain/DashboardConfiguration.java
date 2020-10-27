package de.unipa.hams.hamer.reactive.domain;

import de.unipa.hams.hamer.domain.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(of = "uuid")
public class DashboardConfiguration {
  private String uuid;
  private String owner;
  private List<DashboardNode> nodes;

  public boolean hasNode(Node node) {
    for (DashboardNode dn : nodes) {
      if (dn.getNode().equals(node)) return true;
    }
    return false;
  }
}

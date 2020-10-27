package de.unipa.hams.hamer.reactive.domain;

import de.unipa.hams.hamer.domain.Node;
import lombok.Data;

import java.util.Date;

@Data
public class DashboardNode {

  private Node node;
  private Mode mode;
  private Date historyStart;
  private Date historyEnd;
  private Integer lastX;

  public enum Mode {
    LIVE,
    HIST
  }
}

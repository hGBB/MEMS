package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "threshold"})
public class RuleDesignation {

  @EmbeddedId
  private RuleDesignationKey id;

  @Column(nullable = false)
  private boolean lowerOrEquals = false;

  @Column(nullable = false)
  private double threshold;

  public Node getNode() {
    return id.getNode();
  }

  public Kpi getKpi() {
    return id.getKpi();
  }

  public AlertRule getRule() {
    return id.getRule();
  }
}

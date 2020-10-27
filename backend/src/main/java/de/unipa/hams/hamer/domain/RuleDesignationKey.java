package de.unipa.hams.hamer.domain;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Data
public class RuleDesignationKey implements Serializable {

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private Node node;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private AlertRule rule;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private Kpi kpi;

}

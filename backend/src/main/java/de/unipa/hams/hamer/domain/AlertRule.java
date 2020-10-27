package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "name"})
public class AlertRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private User configuredBy;

  @Column
  private int timespan;

  @Column
  private int sleep;

  public enum Method {
    AVERAGE,
    STANDARD_DEVIATION,
    KURTOSIS
  }

  @Column
  private Method method;
}

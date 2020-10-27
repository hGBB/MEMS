package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "timestamp"})
@ToString(of = "id")
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private long id;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;

  @Column(nullable = false)
  private Double value;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private Kpi type;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn
  private Node sentBy;
}

package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "timestamp"})
public class Audit {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private long id;

  @Column
  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;

  @Column
  private String actionPerformed;

  @Column
  private String performedByName;

  @Column
  private String targetName;

}

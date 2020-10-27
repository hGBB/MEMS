package de.unipa.hams.hamer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
public class Kpi {

  @Getter
  @AllArgsConstructor
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  public enum Unit {
    PERCENT("%"),
    KBIT("kbit/s"),
    MB("MB"),
    WATT("W"),
    VOLT("V"),
    AMPERE("A");

    private String name;
  }


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private long id;

  @Column(nullable = false)
  private String name;

  @Column
  @Enumerated(EnumType.STRING)
  private Unit unit;
}

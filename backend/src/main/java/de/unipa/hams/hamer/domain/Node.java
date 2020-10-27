package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id"})
public class Node {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private int sendInterval;

  @Column(nullable = false)
  private String uuid;

  @ManyToMany(mappedBy = "nodes")
  private Set<UserGroup> groups;

  @Column
  private boolean estimated;

  @Transient
  private boolean registered = true;
}

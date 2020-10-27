package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "name"})
public class UserGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "group_membership",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<User> users;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "node_assignment",
    joinColumns = @JoinColumn(name = "node_id"),
    inverseJoinColumns = @JoinColumn(name = "group_id"))
  private Set<Node> nodes;
}

package de.unipa.hams.hamer.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "name"})
@ToString(exclude = {"groups"})
public class User {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private boolean admin;

  @Column(nullable = false)
  private boolean privileged;

  @ManyToMany(mappedBy = "users")
  private Set<UserGroup> groups;

  public List<GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new LinkedList<>();
    if (admin) authorities.add(new SimpleGrantedAuthority(Authority.ADMIN.getValue()));
    if (privileged) authorities.add(new SimpleGrantedAuthority(Authority.PRIVILEGED.getValue()));
    authorities.add(new SimpleGrantedAuthority(Authority.USER.getValue()));
    return authorities;
  }

  @Getter
  @AllArgsConstructor
  public enum Authority {
    ADMIN("ROLE_ADMIN"),
    PRIVILEGED("ROLE_PRIVILEGED"),
    USER("ROLE_USER");
    private String value;
  }
}

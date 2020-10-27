package de.unipa.hams.hamer.security.service.api;

import de.unipa.hams.hamer.domain.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface AuthorityService {
  boolean hasAccessToNode(@NotNull Node node);

  @NotNull
  Set<Node> nodesForUser();

  boolean hasRole(@NotNull String role);
}

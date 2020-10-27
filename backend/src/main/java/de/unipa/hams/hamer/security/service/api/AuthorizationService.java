package de.unipa.hams.hamer.security.service.api;

import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AuthorizationService {

  @NotNull
  User currentUser() throws AuthorizationContextException;

  @NotNull
  List<UserGroup> currentGroups() throws AuthorizationContextException;

  @NotNull
  Long currentNode() throws AuthorizationContextException;

  @NotNull
  List<Node> currentNodes() throws AuthorizationContextException;

  @NotNull
  Notification currentNotification(@NotNull Long id) throws AuthorizationContextException;

  @NotNull
  List<Notification> currentNotifications() throws AuthorizationContextException;
}


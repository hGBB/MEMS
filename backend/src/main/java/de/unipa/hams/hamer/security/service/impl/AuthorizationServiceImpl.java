package de.unipa.hams.hamer.security.service.impl;

import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.exception.ShouldBeAuthenticatedException;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.persistence.api.NotificationRepository;
import de.unipa.hams.hamer.persistence.api.UserGroupRepository;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationServiceImpl implements AuthorizationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final UserGroupRepository userGroupRepository;
  private final NodeRepository nodeRepository;

  @Override
  @NotNull
  public User currentUser() throws AuthorizationContextException {
    Object principal = principal();
    if (!(principal instanceof JwtUser))
      throw new AuthorizationContextException("No JWT user available in this context.");
    String username = ((JwtUser) principal).getUsername();
    User user = userRepository.findByLogin(username);
    if (user == null) throw new ShouldBeAuthenticatedException("Well we've got an authentication, but there's no" +
      " user to it. Something's terribly wrong...");
    return user;
  }

  @Override
  @NotNull
  public List<UserGroup> currentGroups() throws AuthorizationContextException {
    User user = currentUser();
    return userGroupRepository.findAllByUsersContaining(user);
  }

  @Override
  public @NotNull Long currentNode() throws AuthorizationContextException {
    Object principal = principal();
    if (!(principal instanceof Long))
      throw new AuthorizationContextException("No Node authorization available in this context.");
    return (Long) principal;
  }

  @NotNull
  @Override
  public List<Node> currentNodes() throws AuthorizationContextException {
    if (currentUser().isAdmin()) {
      return nodeRepository.findAll();
    } else {
      return nodeRepository.findAllByGroups(currentGroups());
    }
  }

  @NotNull
  @Override
  public Notification currentNotification(@NotNull Long id) throws AuthorizationContextException {
    return notificationRepository.getByIdAndRecipient(id, currentUser());
  }

  @NotNull
  @Override
  public List<Notification> currentNotifications() throws AuthorizationContextException {
    return notificationRepository.getAllByRecipient(currentUser());
  }

  @NotNull
  private Object principal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null)
      throw new ShouldBeAuthenticatedException("Anytime a user is queried in the system, it must be authenticated.");
    Object principal = authentication.getPrincipal();
    if (principal == null)
      throw new ShouldBeAuthenticatedException("Anytime a user is queried in the system, it must be authenticated." +
        "A user without principal should not occur.");
    return principal;
  }
}

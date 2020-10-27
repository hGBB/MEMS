package de.unipa.hams.hamer.security.service.impl;

import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {

  private final AuthorizationService authorizationService;

  @Override
  @Transactional
  public boolean hasAccessToNode(@NotNull Node node) {
    try {
      for (UserGroup g : authorizationService.currentGroups()) {
        if (g.getNodes().contains(node)) return true;
      }
    } catch (AuthorizationContextException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }

  @NotNull
  @Override
  @Transactional
  public Set<Node> nodesForUser() {
    try {
      Set<Node> nodes = new HashSet<>();
      authorizationService.currentGroups().forEach(i -> nodes.addAll(i.getNodes()));
      return nodes;
    } catch (AuthorizationContextException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return Collections.emptySet();
  }

  @Override
  public boolean hasRole(@NotNull String role) {
    try {
      User u = authorizationService.currentUser();
      return u.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    } catch (AuthorizationContextException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    return false;
  }
}

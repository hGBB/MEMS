package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.controller.dto.mapper.UserMapper;
import de.unipa.hams.hamer.crud.service.api.*;
import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserCrudServiceImpl implements UserCrudService, UserDetailsService {

  @Value("${mems.contextPath}")
  private String contextPath;

  @Value("${mems.port}")
  private Integer port;

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final HamerMailSenderService mailSenderService;
  private final AuthorityService authorityService;
  private final AuthorizationService authorizationService;
  private final AlertCrudService alertCrudService;
  private final GroupCrudService groupCrudService;
  private final NotificationCrudService notificationCrudService;
  private final RuleDesignationService ruleDesignationService;


  private Map<String, String> passwordResets = new HashMap<>();

  @Override
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public @NotNull List<User> getUsers() {
    if (authorityService.hasRole("ROLE_ADMIN")) {
      return userRepository.findAll();
    }
    return userRepository.findAllByGroupsContaining(authorizationService.currentGroups());
  }

  @Override
  public @NotNull User getUser(long id) {
    return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  @Override
  public void resetPassword(@NotNull String login, @NotNull String email) throws MessagingException {
    User u = userRepository.findByLogin(login);
    if (u == null) {
      throw new AccessDeniedException("We do not have this user!");
    }
    if (!email.equals(u.getEmail())) {
      throw new AccessDeniedException("Wrong Email!");
    }
    String uuid = UUID.randomUUID().toString();
    passwordResets.put(uuid, login);
    u.setPassword(passwordEncoder.encode(uuid));
    userRepository.save(u);
    mailSenderService.sendResetPasswordLinkEmail(u, uuid);
  }

  @Override
  @PreAuthorize("permitAll()")
  public void newPassword(@NotNull String token, @NotNull String password) {
    String login = passwordResets.get(token);
    User u = userRepository.findByLogin(login);
    if (u == null) {
      throw new AccessDeniedException("Invalid password reset.");
    }
    u.setPassword(passwordEncoder.encode(password));
    userRepository.save(u);
    passwordResets.remove(token);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public @NotNull User createUser(@NotNull User user, @NotNull String password) throws MessagingException {
    user.setPassword(passwordEncoder.encode(password));

    String uuid = UUID.randomUUID().toString();
    passwordResets.put(uuid, user.getLogin());
    mailSenderService.sendPasswordLinkEmail(user, password, uuid);

    return userRepository.save(user);
  }

  @Override
  public void updateUser(long id, @NotNull User user, @Nullable String password, @Nullable String oldPassword) {
    User u = userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    if (!u.equals(authorizationService.currentUser())) {
      throw new AccessDeniedException("Don't touch other profiles");
    }
    u.setPrivileged(user.isPrivileged());
    u.setAdmin(user.isAdmin());
    u.setEmail(user.getEmail());
    u.setGroups(user.getGroups());
    //  userMapper.mergeDomain(user, u);
    if (password != null) {
      if (passwordEncoder.matches(oldPassword, u.getPassword())) {
        u.setPassword(passwordEncoder.encode(password));
      }
    }
    userRepository.saveAndFlush(u);
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteUser(long id) {
    if (userRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException("User you are attempting to delete does not exist!");
    }
    User user = getUser(id);
    notificationCrudService.deleteAllNotificationsOfUser(user);
    List<RuleDesignation> rules = ruleDesignationService.getAll();
    for (RuleDesignation r : rules) {
      if (r.getRule().getConfiguredBy().equals(user)) {
        ruleDesignationService.deleteRuleDesignation(r.getId());
      }
    }
    List<AlertRule> alerts = alertCrudService.getAllAlertRules(user.getId());
    for (AlertRule a : alerts) {
      alertCrudService.deleteAlertRule(a.getId());
    }
    groupCrudService.removeUserFromUserGroups(user);
    userRepository.deleteById(id);
  }


  @Override
  public JwtUser loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByLogin(username);
    JwtUser jwt = new JwtUser();
    jwt.setAccountNonExpired(true);
    jwt.setAccountNonLocked(true);
    jwt.setApiAccessAllowed(true);
    jwt.setCredentialsNonExpired(true);
    jwt.setEnabled(true);
    jwt.setId(user.getId());
    jwt.setUsername(user.getLogin());
    jwt.setPassword(user.getPassword());
    jwt.setAuthorities(user.getAuthorities());
    return jwt;
  }

}

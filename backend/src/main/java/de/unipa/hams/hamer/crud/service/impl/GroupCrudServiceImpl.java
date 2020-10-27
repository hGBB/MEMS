package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.controller.dto.mapper.GroupMapper;
import de.unipa.hams.hamer.crud.service.api.GroupCrudService;
import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.persistence.api.UserGroupRepository;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupCrudServiceImpl implements GroupCrudService {

  private final UserGroupRepository userGroupRepository;
  private final UserRepository userRepository;
  private final NodeRepository nodeRepository;
  private final GroupMapper groupMapper;
  private final AuthorizationService authorizationService;
  private final AuthorityService authorityService;
  private final RuleDesignationService ruleDesignationService;

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public long createUserGroup(@NotNull UserGroup userGroup) {
    userGroup.setId(null);
    userGroup.setUsers(resolveUsers(userGroup.getUsers()));
    userGroup.setNodes(resolveNodes(userGroup.getNodes()));
    return userGroupRepository.saveAndFlush(userGroup).getId();
  }

  @Override
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public @NotNull UserGroup getUserGroupById(long id) {
    UserGroup group = userGroupRepository.getOne(id);
    if (!group.getUsers().contains(authorizationService.currentUser()) && !authorityService.hasRole("ROLE_ADMIN")) {
      throw new AccessDeniedException("You have no access to this group.");
    }
    return group;
  }

  @Override
  public @NotNull List<UserGroup> getAllUserGroups() {
    if (authorityService.hasRole("ROLE_ADMIN")) {
      return userGroupRepository.findAll();
    }
    return userGroupRepository.findAllByUsersContaining(authorizationService.currentUser());
  }

  @Override
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public @NotNull UserGroup updateUserGroup(long id, UserGroup userGroup) {
    UserGroup usg = userGroupRepository.getOne(id);
    if (authorityService.hasRole("ROLE_PRIVILEGED") && !usg.getUsers().contains(authorizationService.currentUser())) {
      throw new AccessDeniedException("You have no access to this group.");
    }
    Set<User> users = userGroupRepository.getOne(id).getUsers();
    Set<Node> nodes = userGroupRepository.getOne(id).getNodes();

    groupMapper.merge(userGroup, usg);
    usg.setUsers(resolveUsers(userGroup.getUsers()));
    usg.setNodes(resolveNodes(userGroup.getNodes()));

    UserGroup group = userGroupRepository.saveAndFlush(userGroup);

    removeRuleDesignationsIfNodeIsRemovedFromGroup(users, nodes);

    return group;

  }

  private void removeRuleDesignationsIfNodeIsRemovedFromGroup(Set<User> users, Set<Node> nodes) {
    for (User u : users) {
      // get all nodes after usergroup is updated for an user of said group
      List<Node> nodesOfUser = nodeRepository.findAllByGroups(userGroupRepository.findAllByUsersContaining(u));
      // compare nodes with list of nodes of deleted user group
      for (Node n : nodes) {
        if (nodesOfUser.isEmpty() || !nodesOfUser.contains(n)) {
          // if a node doesn't show in the users current nodes delete all rule designations containing that user and node
          List<RuleDesignation> rules = ruleDesignationService.getAll();
          for (RuleDesignation r : rules) {
            if (r.getNode().equals(n) && r.getRule().getConfiguredBy().equals(u)) {
              ruleDesignationService.deleteRuleDesignation(r.getId());
            }
          }
        }
      }
    }
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteUserGroup(long id) {
    if (userGroupRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException("Alert Rule with ID " + id + " does not exist!");
    }
    Set<User> users = userGroupRepository.getOne(id).getUsers();
    Set<Node> nodes = userGroupRepository.getOne(id).getNodes();
    userGroupRepository.deleteById(id);
    removeRuleDesignationsIfNodeIsRemovedFromGroup(users, nodes);
  }

  @Override
  public void removeUserFromUserGroups(User user) {
    List<UserGroup> groups = userGroupRepository.findAllByUsersContaining(user);
    for (UserGroup group : groups) {
      Set<User> users = group.getUsers();
      users.remove(user);
      group.setUsers(users);
      userGroupRepository.saveAndFlush(group);
    }
  }

  private Set<User> resolveUsers(Set<User> users) {
    Set<User> newUsers = new HashSet<>();
    for (User u : users) {
      if (u.getId() != null) {
        User us = userRepository.getOne(u.getId());
        newUsers.add(us);
      }
    }
    return newUsers;
  }

  private Set<Node> resolveNodes(Set<Node> nodes) {
    Set<Node> newNodes = new HashSet<>();
    for (Node n : nodes) {
      if (n.getId() != null) {
        newNodes.add(nodeRepository.getOne(n.getId()));
      }
    }
    return newNodes;
  }


}


package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GroupCrudService {

  long createUserGroup(@NotNull UserGroup userGroup);

  @NotNull
  UserGroup getUserGroupById(long id);

  @NotNull
  List<UserGroup> getAllUserGroups();

  @NotNull
  UserGroup updateUserGroup(long id, UserGroup userGroup);

  void deleteUserGroup(long id);

  void removeUserFromUserGroups(User user);
}

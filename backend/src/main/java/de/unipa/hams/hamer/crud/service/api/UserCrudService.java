package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.mail.MessagingException;
import java.util.List;

public interface UserCrudService {
  @NotNull
  List<User> getUsers();

  @NotNull
  User getUser(long id);

  void resetPassword(@NotNull String login, @NotNull String email) throws MessagingException;

  void newPassword(@NotNull String token, @NotNull String password);

  @NotNull
  User createUser(@NotNull User user, @NotNull String password) throws MessagingException;

  void updateUser(long id, @NotNull User user, @Nullable String password, @Nullable String oldPassword);

  void deleteUser(long id);

}

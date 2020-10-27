package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NotificationCrudService {

  @NotNull
  Notification getNotificationById(@NotNull Long id);

  @NotNull
  List<Notification> getAllNotifications();

  void deleteNotification(@NotNull Long id);

  void deleteAllNotificationsOfUser(@NotNull User user);
}

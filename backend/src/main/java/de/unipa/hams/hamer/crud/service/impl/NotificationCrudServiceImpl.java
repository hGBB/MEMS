package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.NotificationCrudService;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.persistence.api.NotificationRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationCrudServiceImpl implements NotificationCrudService {

  private NotificationRepository notificationRepository;

  @Override
  public @NotNull Notification getNotificationById(@NotNull Long id) {
    if (notificationRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException();
    } else {
      return notificationRepository.getOne(id);
    }
  }

  @Override
  public @NotNull List<Notification> getAllNotifications() {
    return notificationRepository.findAll();
  }

  @Override
  public void deleteNotification(@NotNull Long id) {
    notificationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    notificationRepository.deleteById(id);
  }

  @Override
  public void deleteAllNotificationsOfUser(@NotNull User user) {
    notificationRepository.deleteAllByRecipient(user);
  }


}

package de.unipa.hams.hamer.reactive.service.api;

import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.Record;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface NotificationService {

  @NotNull
  List<Notification> notificationsFor(@NotNull Record record);

}

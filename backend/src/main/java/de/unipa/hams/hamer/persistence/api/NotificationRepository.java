package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @NotNull
  List<Notification> getAllByTimestampAfterAndRecipientAndTriggeredBy(@NotNull Date date, @NotNull User user, @NotNull AlertRule triggeredBy);

  @NotNull
  Notification getByIdAndRecipient(@NotNull Long id, @NotNull User user);

  @NotNull
  List<Notification> getAllByRecipient(@NotNull User user);

  @NotNull
  List<Notification> getAllByTimestampIsAfterAndRecipient(@NotNull Date timestamp, @NotNull User recipient);

  void deleteAllByRecipient(@NotNull User user);
}

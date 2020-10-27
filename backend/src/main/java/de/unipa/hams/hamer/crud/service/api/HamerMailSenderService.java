package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;

import javax.mail.MessagingException;

public interface HamerMailSenderService {

  void sendPasswordLinkEmail(@NotNull User user, @NotNull String password, @NotNull String uuid) throws MessagingException;

  void sendResetPasswordLinkEmail(@NotNull User user, @NotNull String uuid) throws MessagingException;

  void sendAlertEmail(@NotNull RuleDesignation rule, @NotNull String value) throws MessagingException;

  void sendNotificationEmail(@NotNull Notification notification) throws MessagingException;
}

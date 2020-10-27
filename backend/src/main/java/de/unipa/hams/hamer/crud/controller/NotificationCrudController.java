package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.NotificationDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NotificationMapper;
import de.unipa.hams.hamer.crud.service.api.NotificationCrudService;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationCrudController {

  private final AuthorizationService authorizationService;
  private NotificationCrudService notificationCrudService;
  private NotificationMapper notificationMapper;

  @NotNull
  @GetMapping("/notifications/{id}")
  @ResponseStatus(HttpStatus.OK)
  public NotificationDTO getNotification(@NotNull @PathVariable("id") Long id) {
    return notificationMapper.toDTO(authorizationService.currentNotification(id));
  }

  @NotNull
  @GetMapping("/notifications")
  @ResponseStatus(HttpStatus.OK)
  public List<NotificationDTO> getNotifications() {
    List<NotificationDTO> result = new LinkedList<>();
    for (Notification n : authorizationService.currentNotifications()) {
      result.add(notificationMapper.toDTO(n));
    }
    return result;
  }

  @DeleteMapping("/notifications/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteNotification(@NotNull @PathVariable("id") Long id) {
    notificationCrudService.deleteNotification(id);
  }
}

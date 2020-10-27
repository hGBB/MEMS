package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.NotificationDTO;
import de.unipa.hams.hamer.domain.Notification;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {

  @NotNull
  @Mapping(source = "alertRuleId", target = "triggeredBy", ignore = true)
  public abstract Notification toEntity(NotificationDTO notificationDTO);

  @NotNull
  @Mapping(target = "timestamp", dateFormat = "yyyy.MM.dd HH:mm")
  @Mapping(source = "triggeredBy.id", target = "alertRuleId")
  @Mapping(source = "triggeredBy.name", target = "alertRuleName")
  public abstract NotificationDTO toDTO(Notification notification);
}

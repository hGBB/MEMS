package de.unipa.hams.hamer.reactive.controller;

import de.unipa.hams.hamer.event.RecordReceived;
import de.unipa.hams.hamer.reactive.controller.dto.DashboardDTO;
import de.unipa.hams.hamer.reactive.controller.dto.DashboardMapper;
import de.unipa.hams.hamer.reactive.controller.dto.DashboardValueMapper;
import de.unipa.hams.hamer.reactive.domain.DashboardValue;
import de.unipa.hams.hamer.reactive.service.api.DashboardService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DashboardController {

  private DashboardMapper dashboardMapper;
  private DashboardService dashboardService;
  private SimpMessagingTemplate messagingTemplate;
  private DashboardValueMapper dashboardValueMapper;

  /**
   * Register a DashboardConfiguration. One per User.
   *
   * @param dashboardDTO Configuration of DashboardConfiguration to register.
   */
  @MessageMapping("/configure")
  public void registerDashboard(@Payload @NotNull DashboardDTO dashboardDTO) {
    dashboardService.activateUserDashboard(dashboardMapper.toDomain(dashboardDTO));
  }

  @EventListener
  public void unregisterDashboard(SessionDisconnectEvent event) {
    dashboardService.deactivateUserDashboard();
  }

  @EventListener
  @Transactional
  public void onRecordReceived(RecordReceived event) {
    for (DashboardValue dv : dashboardService.dashboardsFor(event.getRecord())) {
      messagingTemplate.convertAndSendToUser(dv.getUser(), "/topic/dashboard", dashboardValueMapper.toDto(dv));
    }
  }

}

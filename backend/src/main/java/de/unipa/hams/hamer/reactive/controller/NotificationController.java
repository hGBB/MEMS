package de.unipa.hams.hamer.reactive.controller;

import de.unipa.hams.hamer.crud.service.api.HamerMailSenderService;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.event.RecordReceived;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.reactive.service.api.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {

  private final NotificationService notificationService;
  private final HamerMailSenderService mailSenderService;
  private final NodeRepository nodeRepository;
  private final RecordRepository recordRepository;
  private final UserRepository userRepository;

  private List<Long> lostNodes = new LinkedList<>();

  @EventListener
  public void onRecordReceived(RecordReceived event) throws MessagingException {
    List<Notification> notifications = notificationService.notificationsFor(event.getRecord());
    for (Notification notification : notifications) {
      mailSenderService.sendNotificationEmail(notification);
    }
  }

  @Scheduled(cron = "0 */2 * * * ?")
  private void checkForRespondingNodes() throws MessagingException {
    List<Node> nodes = nodeRepository.findAll();
    for (Node n : nodes) {
      List<Record> records = recordRepository.getAllByTimestampAfterAndSentBy(DateUtils.addSeconds(new Date(), -(n.getSendInterval() * 2)), n);
      if (!lostNodes.contains(n.getId()) && (records.size() == 0)) {
        List<User> admins = userRepository.findAllByAdminIs(true);
        for (User admin : admins) {
          Notification no = new Notification();
          no.setTimestamp(new Date());
          no.setTriggeredBy(null);
          no.setRecipient(admin);
          no.setText("The node " + n.getName() + " has broken off the communication!");
          mailSenderService.sendNotificationEmail(no);
        }
        lostNodes.add(n.getId());
      }
    }
  }
}

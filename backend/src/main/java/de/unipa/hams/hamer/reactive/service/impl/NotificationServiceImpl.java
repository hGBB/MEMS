package de.unipa.hams.hamer.reactive.service.impl;

import de.unipa.hams.hamer.domain.*;
import de.unipa.hams.hamer.persistence.api.*;
import de.unipa.hams.hamer.reactive.service.api.NotificationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationServiceImpl implements NotificationService {

  private final RuleDesignationRepository ruleDesignationRepository;
  private final RecordRepository recordRepository;
  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NodeRepository nodeRepository;

  private List<Long> lostNodes = new LinkedList<>();

  @Override
  @Transactional
  public @NotNull List<Notification> notificationsFor(@NotNull Record record) {
    var node = record.getSentBy();
    var kpi = record.getType();
    var notifications = new LinkedList<Notification>();
    for (RuleDesignation rd : ruleDesignationRepository.findAll()) {
      if (rd.getNode().equals(node) && rd.getKpi().equals(kpi)) {
        List<Notification> no = notificationRepository.getAllByTimestampAfterAndRecipientAndTriggeredBy(DateUtils.addMinutes(new Date(), -(rd.getRule().getSleep())), rd.getRule().getConfiguredBy(), rd.getRule());
        if (no.size() == 0) {
          var records = collect(rd, node, kpi);
          var user = rd.getRule().getConfiguredBy();
          var val = evaluate(rd.getRule(), records);
          if ((rd.isLowerOrEquals() && val <= rd.getThreshold()) || (!rd.isLowerOrEquals() && val > rd.getThreshold())) {
            var noti = new Notification();
            noti.setRecipient(user);
            noti.setTriggeredBy(rd.getRule());
            noti.setText("Triggered Kpi: " + kpi.getName() + "\n" +
              "Current value: " + val + "(Threshold: " + rd.getThreshold() + " in " + kpi.getUnit() + ")" +
              "Affected node: " + node.getName());
            notificationRepository.save(noti);
            notifications.add(noti);
          }
        }
      }
    }
    return notifications;
  }

  private List<Record> collect(RuleDesignation rd, Node sentBy, Kpi type) {
    var after = DateUtils.addMinutes(new Date(), -(rd.getRule().getTimespan()));
    return recordRepository.getAllByTimestampAfterAndSentByAndType(after, sentBy, type);
  }

  private Double evaluate(AlertRule alertRule, List<Record> records) {
    DescriptiveStatistics s = new DescriptiveStatistics();
    records.forEach(r -> s.addValue(r.getValue()));
    switch (alertRule.getMethod()) {
      case AVERAGE:
        return s.getMean();
      case STANDARD_DEVIATION:
        return s.getStandardDeviation();
      case KURTOSIS:
        return s.getKurtosis();
    }
    return 0.0;
  }

}

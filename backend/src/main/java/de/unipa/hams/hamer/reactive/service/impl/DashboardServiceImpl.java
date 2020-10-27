package de.unipa.hams.hamer.reactive.service.impl;

import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import de.unipa.hams.hamer.reactive.domain.DashboardConfiguration;
import de.unipa.hams.hamer.reactive.domain.DashboardNode;
import de.unipa.hams.hamer.reactive.domain.DashboardNodeValue;
import de.unipa.hams.hamer.reactive.domain.DashboardValue;
import de.unipa.hams.hamer.reactive.service.api.DashboardService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class DashboardServiceImpl implements DashboardService {

  private final AuthorizationService authorizationService;
  private final RecordRepository recordRepository;

  private Map<User, DashboardConfiguration> userDashboards = new HashMap<>();

  @Override
  @Transactional
  public void activateUserDashboard(DashboardConfiguration dashboardConfiguration) {
    var user = authorizationService.currentUser();
    dashboardConfiguration.setOwner(user.getLogin());
    userDashboards.put(user, dashboardConfiguration);
  }

  @Override
  public void deactivateUserDashboard() {
    userDashboards.remove(authorizationService.currentUser());
  }

  @Override
  public List<DashboardValue> dashboardsFor(Record record) {
    List<DashboardValue> res = new LinkedList<>();
    for (User u : userDashboards.keySet()) {
      DashboardConfiguration dc = userDashboards.get(u);
      if (dc.hasNode(record.getSentBy())) {
        res.add(packDashboard(dc));
      }
    }
    return res;
  }

  private DashboardValue packDashboard(DashboardConfiguration dashboardConfiguration) {
    DashboardValue dashboardValue = new DashboardValue(UUID.randomUUID().toString(), dashboardConfiguration.getOwner());
    for (DashboardNode dn : dashboardConfiguration.getNodes()) {
      DashboardNodeValue dashboardNodeValue = new DashboardNodeValue(dn.getNode());
      if (dn.getMode().equals(DashboardNode.Mode.LIVE)) {
        // Live
        Date start = DateUtils.addMinutes(new Date(), -(dn.getLastX()));
        dashboardNodeValue.getRecords().addAll(recordRepository.getAllByTimestampAfterAndSentBy(start, dn.getNode()));
      } else if (dn.getMode().equals(DashboardNode.Mode.HIST)) {
        // Hist
        dashboardNodeValue.getRecords().addAll(recordRepository.getAllByTimestampBetweenAndSentBy(dn.getHistoryStart(), dn.getHistoryEnd(), dn.getNode()));
      }
      dashboardValue.getNodes().add(dashboardNodeValue);
    }
    return dashboardValue;
  }

}

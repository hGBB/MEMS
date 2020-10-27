package de.unipa.hams.hamer.reactive.service.api;

import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.reactive.domain.DashboardConfiguration;
import de.unipa.hams.hamer.reactive.domain.DashboardValue;

import java.util.List;

public interface DashboardService {

  void activateUserDashboard(DashboardConfiguration dashboardConfiguration);

  void deactivateUserDashboard();

  List<DashboardValue> dashboardsFor(Record record);
}

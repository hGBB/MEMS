package de.unipa.hams.hamer.monitoring.service.api;

import de.unipa.hams.hamer.domain.Record;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface MonitoringService {

  void addRecord(@NotNull Record record);

  double estimatePower(@NotNull String string, @NotNull Date date);
}

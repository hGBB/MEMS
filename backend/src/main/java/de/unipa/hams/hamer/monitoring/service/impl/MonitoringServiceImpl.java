package de.unipa.hams.hamer.monitoring.service.impl;

import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.event.RecordReceived;
import de.unipa.hams.hamer.monitoring.service.api.MonitoringService;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonitoringServiceImpl implements MonitoringService {

  private final RecordRepository recordRepository;
  private final KpiRepository kpiRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public void addRecord(@NotNull Record record) {
    recordRepository.save(record);
    eventPublisher.publishEvent(new RecordReceived(this, record));
  }

  @Override
  public double estimatePower(@NotNull String string, @NotNull Date date) {
    Kpi cpuKpi = kpiRepository.findByName("cpu");
    int cpuValue = recordRepository.getByTimestampAndType(date, cpuKpi).getValue().intValue();
    if (string.equals("power_usage")) {
      double baseline = 2.1;
      baseline = baseline + (2.8 * cpuValue / 100 - (1.0 / 100 * Math.random()));
      baseline = (double) Math.round(baseline * 1000d) / 1000d;
      return baseline;
    }
    if (string.equals("power_voltage")) {
      double baseline = 228;
      baseline = baseline + (1.7 * cpuValue / 100 - (1.0 / 1000 * Math.random()));
      baseline = (double) Math.round(baseline * 10000d) / 10000d;
      return baseline;
    }
    if (string.equals("power_current")) {
      double baseline = 0.017;
      baseline = baseline + (0.02 * cpuValue / 100 - (1.0 / 10000 * Math.random()));
      baseline = (double) Math.round(baseline * 100000d) / 100000d;
      return baseline;
    }
    return 0;
  }
}

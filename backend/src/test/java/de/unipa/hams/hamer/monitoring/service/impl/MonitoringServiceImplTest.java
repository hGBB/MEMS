package de.unipa.hams.hamer.monitoring.service.impl;

import de.unipa.hams.hamer.monitoring.service.api.MonitoringService;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MonitoringServiceImplTest {

  @Mock
  private static RecordRepository recordRepository;

  @Mock
  private static KpiRepository kpiRepository;

  @Autowired
  private MonitoringService monitoringService;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void registerRecordUpdateListener() {
  }

  @Test
  public void addRecord() {
  }

  @Test
  public void estimatePower() {
  }

  @TestConfiguration
  static class MonitoringServiceBeanTestContextConfiguration {
    @Bean
    public MonitoringServiceImpl monitoringService() {
      return new MonitoringServiceImpl(recordRepository, kpiRepository, null);
    }
  }
}

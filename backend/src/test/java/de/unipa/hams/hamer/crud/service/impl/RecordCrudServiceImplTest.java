package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.RecordCrudService;
import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class RecordCrudServiceImplTest {

  private static final Kpi TYPE = new Kpi();
  private static final Record RECORD1 = new Record();
  private static final Date DATE = new Date();

  @Mock
  private static RecordRepository recordRepository;

  @Autowired
  private RecordCrudService recordCrudService;

  @Before
  public void setUp() throws Exception {
    RECORD1.setId(1);
    given(recordRepository.save(RECORD1)).willReturn(RECORD1);
    given(recordRepository.getByTimestampAndType(DATE, TYPE)).willReturn(RECORD1);
  }

  @Test
  public void createRecord() {
    assertThat(recordCrudService.createRecord(RECORD1)).isEqualTo(1);
  }

  @Test
  public void getByTimestampAndType() {
    assertThat(recordCrudService.getByTimestampAndType(DATE, TYPE)).isEqualTo(RECORD1);
  }

  @TestConfiguration
  static class RecordCrudServiceBeanTestContextConfiguration {
    @Bean
    public RecordCrudService recordCrudService() {
      return new RecordCrudServiceImpl(recordRepository);
    }
  }
}

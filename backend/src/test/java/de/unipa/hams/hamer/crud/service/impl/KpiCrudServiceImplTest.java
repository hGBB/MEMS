package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.KpiCrudService;
import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class KpiCrudServiceImplTest {

  @Mock
  private static KpiRepository kpiRepository;
  private static final Kpi KPI1 = new Kpi();
  private static final Kpi KPI2 = new Kpi();
  private static final List<Kpi> KPIs = new LinkedList<>() {{
    add(KPI1);
    add(KPI2);
  }};

  @Autowired
  private KpiCrudService kpiCrudService;

  @Before
  public void setUp() throws Exception {
    given(kpiRepository.findAll()).willReturn(KPIs);
    given(kpiRepository.findByName("test")).willReturn(KPI1);
  }

  @Test
  public void getKpis() {
    assertThat(kpiCrudService.getKpis()).isSameAs(KPIs);
  }

  @Test
  public void getByKpiName() {
    assertThat(kpiCrudService.getByKpiName("test")).isEqualTo(KPI1);
  }

  @TestConfiguration
  static class KpiCrudServiceBeanTestContextConfiguration {
    @Bean
    public KpiCrudService kpiCrudService() {
      return new KpiCrudServiceImpl(kpiRepository);
    }
  }
}

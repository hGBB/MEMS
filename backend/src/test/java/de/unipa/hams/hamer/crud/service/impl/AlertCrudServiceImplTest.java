package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.AlertCrudService;
import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.persistence.api.AlertRuleRepository;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.ignoreStubs;

@RunWith(SpringRunner.class)
public class AlertCrudServiceImplTest {

  private static final AlertRule AR1 = new AlertRule();
  private static final AlertRule AR2 = new AlertRule();
  private static final List<AlertRule> AL = new LinkedList<>() {{
    add(AR1);
    add(AR2);
  }};
  @Mock
  private static AlertRuleRepository alertRuleRepository;
  @Mock
  private static AuthorizationService userService;
  @Autowired
  private AlertCrudService alertCrudService;

  @Before
  public void setUp() throws Exception {
    ignoreStubs(userService);
    AR1.setId(1L);
    given(alertRuleRepository.saveAndFlush(AR1)).willReturn(AR1);
    given(alertRuleRepository.getOne(1L)).willReturn(AR1);
    given(alertRuleRepository.findAll()).willReturn(AL);
    given(alertRuleRepository.findAllByConfiguredBy_Id(1L)).willReturn(AL);
    given(alertRuleRepository.findById(1L)).willReturn(java.util.Optional.of(AR1));

  }

  @Test
  public void createAlertRule() {
    assertThat(alertCrudService.createAlertRule(AR1)).isEqualTo(1L);

  }

  @Test
  public void getAlertRuleById() {
    assertThat(alertCrudService.getAlertRuleById(1L)).isEqualTo(AR1);
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> alertCrudService.getAlertRuleById(5L));
  }

  @Test
  public void getAllAlertRules() {
    assertThat(alertCrudService.getAllAlertRules(1)).isSameAs(AL);
  }

  @Test
  public void updateAlertRule() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> alertCrudService.updateAlertRule(5L, AR1));
  }

  @Test
  public void deleteAlertRule() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> alertCrudService.deleteAlertRule(5L));
  }

  @Test
  public void createEmail() {
  }

  @Test
  public void createResetPasswordEmail() {
  }

  @TestConfiguration
  static class AlertCrudServiceBeanTestContextConfiguration {
    @Bean
    public AlertCrudServiceImpl alertCrudService() {
      return new AlertCrudServiceImpl(alertRuleRepository, userService);
    }
  }
}

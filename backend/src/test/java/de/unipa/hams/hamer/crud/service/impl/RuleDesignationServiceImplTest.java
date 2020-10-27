package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.RuleDesignationKey;
import de.unipa.hams.hamer.persistence.api.RuleDesignationRepository;
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

@RunWith(SpringRunner.class)
public class RuleDesignationServiceImplTest {

  private static final RuleDesignation R1 = new RuleDesignation();
  private static final RuleDesignation R2 = new RuleDesignation();
  private static final RuleDesignationKey KEY = new RuleDesignationKey();
  private static final RuleDesignationKey KEY2 = new RuleDesignationKey();
  private static final List<RuleDesignation> RL = new LinkedList<>() {{
    add(R1);
    add(R2);
  }};

  @Mock
  private static RuleDesignationRepository ruleDesignationRepository;

  @Autowired
  private RuleDesignationService ruleDesignationService;

  @Before
  public void setUp() throws Exception {
    given(ruleDesignationRepository.findAll()).willReturn(RL);
    given(ruleDesignationRepository.findById(KEY)).willReturn(java.util.Optional.of(R1));
    given(ruleDesignationRepository.findById(KEY2)).willReturn(java.util.Optional.empty());
  }

  @Test
  public void createRuleDesignation() {
  }

  @Test
  public void findByIdExists() {
    assertThat(ruleDesignationService.findByIdExists(KEY2)).isFalse();
//    assertThat(ruleDesignationService.findByIdExists(KEY)).isTrue();
  }

  @Test
  public void updateRuleDesignation() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> ruleDesignationService.deleteRuleDesignation(KEY2));
  }

  @Test
  public void getAll() {
    assertThat(ruleDesignationService.getAll()).isSameAs(RL);
  }

  @Test
  public void deleteRuleDesignation() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> ruleDesignationService.deleteRuleDesignation(KEY2));
  }

  @TestConfiguration
  static class RuleDesignationServiceBeanTestContextConfiguration {
    @Bean
    RuleDesignationService ruleDesignationService() {
      return new RuleDesignationServiceImpl(ruleDesignationRepository);
    }
  }


}

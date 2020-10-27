package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.AuditCrudService;
import de.unipa.hams.hamer.domain.Audit;
import de.unipa.hams.hamer.persistence.api.AuditRepository;
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
public class AuditCrudServiceImplTest {

  private static final Audit A1 = new Audit();
  private static final Audit A2 = new Audit();
  private static final List<Audit> AL = new LinkedList<>() {{
    add(A1);
    add(A2);
  }};

  @Mock
  private static AuditRepository auditRepository;

  @Autowired
  private AuditCrudService auditCrudService;

  @Before
  public void setUp() throws Exception {
    A1.setId(1L);
    given(auditRepository.getOne(1L)).willReturn(A1);
    given(auditRepository.findAll()).willReturn(AL);
    given(auditRepository.saveAndFlush(A1)).willReturn(A1);

  }

  @Test
  public void createAudit() {
    assertThat(auditCrudService.createAudit(A1)).isEqualTo(1L);
  }

  @Test
  public void getAllAudits() {
    assertThat(auditCrudService.getAllAudits()).isSameAs(AL);
  }

  @TestConfiguration
  static class AuditCrudServiceBeanTestContextConfiguration {
    @Bean
    public AuditCrudService auditCrudService() {
      return new AuditCrudServiceImpl(auditRepository);
    }
  }

}

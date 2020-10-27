package de.unipa.hams.hamer.monitoring.controller;

import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.KpiCrudService;
import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.monitoring.service.api.MonitoringService;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MonitoringController.class)
@AutoConfigureMockMvc(secure = false)
public class MonitoringControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MonitoringService monitoringService;

  @MockBean
  private KpiCrudService kpiCrudService;

  @MockBean
  private NodeCrudService nodeCrudService;

  @MockBean
  private NodeRepository nodeRepository;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private ObjectPostProcessor<?> objectPostProcessor;

  @MockBean
  private AuthenticationConfiguration authenticationConfiguration;

  @MockBean
  private AuthorizationService authorizationService;

  @MockBean
  private AuthorityService authorityService;

  @MockBean
  private NodeMapper nodeMapper;

  @MockBean
  private KpiRepository kpiRepository;

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void configurationChanged() {

  }

  @Test
  public void receiveKpis() {

  }
}

package de.unipa.hams.hamer.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.unipa.hams.hamer.crud.controller.dto.AlertRuleDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.AlertMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.KpiMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.AlertCrudService;
import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.crud.service.api.UserCrudService;
import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.domain.User;
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
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AlertCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class AlertCrudControllerTest {

  private static final AlertRule DUMMY1 = new AlertRule();
  private static final AlertRule DUMMY2 = new AlertRule();
  private static final AlertRuleDTO DUMMYDTO1 = new AlertRuleDTO();
  private static final AlertRuleDTO DUMMYDTO2 = new AlertRuleDTO();
  private static final List<AlertRule> ALERT_RULES = new LinkedList<>() {{
    add(DUMMY1);
    add(DUMMY2);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AlertCrudService alertCrudService;

  @MockBean
  private AlertMapper alertMapper;

  @MockBean
  private KpiMapper kpiMapper;

  @MockBean
  private RuleDesignationService ruleDesignationService;

  @MockBean
  private UserCrudService userService;

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

  @Before
  public void setUp() throws Exception {
    DUMMYDTO1.setKpis(new LinkedList<>());
    DUMMYDTO1.setNodes(new LinkedList<>());
    User user = new User();
    user.setId(1L);
    DUMMY1.setConfiguredBy(user);
    given(alertMapper.toEntity(DUMMYDTO1)).willReturn(DUMMY1);
    given(alertMapper.toEntity(DUMMYDTO2)).willReturn(DUMMY2);
    given(alertMapper.toDto(DUMMY1)).willReturn(DUMMYDTO1);
    given(alertMapper.toDto(DUMMY2)).willReturn(DUMMYDTO2);
    given(alertCrudService.createAlertRule(DUMMY1)).willReturn(1L);
    given(alertCrudService.getAllAlertRules(1)).willReturn(ALERT_RULES);
    given(alertCrudService.getAlertRuleById(1)).willReturn(DUMMY1);
    given(ruleDesignationService.getAll()).willReturn(new LinkedList<>());
    given(authorizationService.currentUser()).willReturn(user);
    given(alertCrudService.getAllAlertRules(user.getId())).willReturn(ALERT_RULES);

  }

  @Test
  public void createAlert() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMYDTO1);
    mockMvc.perform(post("/api/alerts").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", "http://localhost/api/alerts/1"));
  }

  @Test
  public void getAlerts() throws Exception {
    mockMvc.perform(get("/api/alerts").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  public void getAlert() throws Exception {
    mockMvc.perform(get("/api/alerts/1").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").exists())
      .andExpect(jsonPath("$").isNotEmpty());

  }

  @Test
  public void updateAlert() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMYDTO1);
    mockMvc.perform(put("/api/alerts/1").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isOk());
  }

  @Test
  public void deleteAlert() throws Exception {
    mockMvc.perform(delete("/api/alerts/1").with(user("admin").roles("ADMIN")))
      .andExpect(status().isNoContent());
  }
}

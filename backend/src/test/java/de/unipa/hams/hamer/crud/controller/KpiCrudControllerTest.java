package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.KpiDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.KpiMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.KpiCrudService;
import de.unipa.hams.hamer.domain.Kpi;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(KpiCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class KpiCrudControllerTest {

  private static final Kpi EMPTY_KPI = new Kpi();
  private static final Kpi DUMMY_KPI = new Kpi();
  private static final KpiDTO EMPTY_KPIDTO = new KpiDTO();
  private static final KpiDTO DUMMY_KPIDTO = new KpiDTO();
  private static final List<Kpi> DUMMY_LIST = new LinkedList<>() {{
    add(EMPTY_KPI);
    add(DUMMY_KPI);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private KpiCrudService kpiCrudService;

  @MockBean
  private KpiMapper kpiMapper;

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
    given(kpiCrudService.getKpis()).willReturn(DUMMY_LIST);
    given(kpiMapper.toDto(EMPTY_KPI)).willReturn(EMPTY_KPIDTO);
    given(kpiMapper.toDto(DUMMY_KPI)).willReturn(DUMMY_KPIDTO);
  }

  @Test
  public void getKpis() throws Exception {
    mockMvc.perform(get("/api/kpi").accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty());
  }
}

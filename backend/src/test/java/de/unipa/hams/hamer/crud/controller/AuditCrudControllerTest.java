package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.AuditDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.AuditMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.AuditCrudService;
import de.unipa.hams.hamer.domain.Audit;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuditCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class AuditCrudControllerTest {

  private static final Audit DUMMY_AUDIT = new Audit();
  private static final Audit EMPTY_AUDIT = new Audit();
  private static final AuditDTO DUMMY_AUDITDTO = new AuditDTO();
  private static final AuditDTO EMTPY_AUDITDTO = new AuditDTO();
  private static final List<Audit> DUMMY_LIST = new LinkedList<>() {{
    add(DUMMY_AUDIT);
    add(EMPTY_AUDIT);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuditCrudService auditCrudService;

  @MockBean
  private AuditMapper auditMapper;

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
    given(auditMapper.toDTO(DUMMY_AUDIT)).willReturn(DUMMY_AUDITDTO);
    given(auditMapper.toDTO(EMPTY_AUDIT)).willReturn(EMTPY_AUDITDTO);
    given(auditCrudService.getAllAudits()).willReturn(DUMMY_LIST);
  }

  @Test
  public void getAudit() throws Exception {
    mockMvc.perform(get("/api/audit").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty());

  }
}

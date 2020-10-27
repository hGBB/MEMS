package de.unipa.hams.hamer.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.unipa.hams.hamer.crud.controller.dto.UserGroupDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.GroupMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.GroupCrudService;
import de.unipa.hams.hamer.domain.UserGroup;
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
@WebMvcTest(GroupCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class GroupCrudControllerTest {

  private static final UserGroupDTO EMPTY_GR_DTO = new UserGroupDTO();
  private static final UserGroupDTO DUMMY_GR_DTO = new UserGroupDTO();
  private static final UserGroup EMPTY_GR = new UserGroup();
  private static final UserGroup DUMMY_GR = new UserGroup();
  private static final List<UserGroup> DUMMY_LIST = new LinkedList<>() {{
    add(EMPTY_GR);
    add(DUMMY_GR);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private GroupCrudService groupCrudService;

  @MockBean
  private GroupMapper groupMapper;

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
  public void setUp() {
    given(groupCrudService.createUserGroup(EMPTY_GR)).willReturn(1L);
    given(groupMapper.toEntity(EMPTY_GR_DTO)).willReturn(EMPTY_GR);
    given(groupMapper.toDto(DUMMY_GR)).willReturn(DUMMY_GR_DTO);
    given(groupCrudService.getUserGroupById(2)).willReturn(DUMMY_GR);
    given(groupCrudService.getUserGroupById(1)).willReturn(EMPTY_GR);
    given(groupCrudService.getAllUserGroups()).willReturn(DUMMY_LIST);
    // given(groupCrudService.updateUserGroup(1, EMPTY_GR));

  }

  @Test
  public void createGroup() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_GR);
    mockMvc.perform(post("/api/groups").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", "http://localhost/api/groups/1"));
  }

  @Test
  public void getGroups() throws Exception {
    mockMvc.perform(get("/api/groups").with(user("admin").roles("ADMIN")).accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(2)))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty());// trivial for this kind of test but keep it in mind for integration tests!
  }

  @Test
  public void getGroup() throws Exception {
    mockMvc.perform(get("/api/groups/2").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").exists())
      .andExpect(jsonPath("$").isNotEmpty());
  }


  @Test
  public void updateGroup() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_GR);
    mockMvc.perform(put("/api/groups/{id}", 1)
      .with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isOk());
  }

  @Test
  public void deleteGroup()throws Exception {
    mockMvc.perform(delete("/api/groups/2").with(user("admin").roles("ADMIN")))
      .andExpect(status().isNoContent());
  }
}

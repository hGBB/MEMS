package de.unipa.hams.hamer.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.unipa.hams.hamer.crud.controller.dto.NodeDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodePinMapper;
import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.domain.Node;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NodeCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class NodeCrudControllerTest {

  private static final Node DUMMYNODE1 = new Node();
  private static final Node DUMMYNODE2 = new Node();
  private static final NodeDTO DUMMYNODEDTO1 = new NodeDTO();
  private static final NodeDTO DUMMYNODEDTO2 = new NodeDTO();
  private static final List<Node> DUMMY_LIST = new LinkedList<>() {{
    add(DUMMYNODE1);
    add(DUMMYNODE2);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NodeCrudService nodeCrudService;

  @MockBean
  private NodeMapper nodeMapper;

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
  private NodePinMapper nodePinMapper;

  @Before
  public void setUp() throws Exception {
    Node n1 = new Node();
    n1.setId(1L);
    n1.setName("Node 1");
    n1.setSendInterval(42);
    n1.setUuid("hello word");

    NodeDTO nd1 = new NodeDTO();
    nd1.setId(1);
    nd1.setName("Node 1");
    nd1.setSendInterval(42);

    List<Node> nodes = new LinkedList<>();
    nodes.add(n1);
    given(nodeCrudService.getNodes()).willReturn(nodes);
    given(nodeCrudService.registerNode("test", "test", true)).willReturn(n1);
    given(nodeMapper.entityToDTO(n1)).willReturn(nd1);
    given(authorizationService.currentNodes()).willReturn(nodes);
  }

  @Test
  public void getNodes() throws Exception {
    mockMvc.perform(get("/api/nodes").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty())
      .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  public void registerNode() throws Exception {
    mockMvc.perform(post("/api/nodes").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .param("key", "test")
      .param("name", "test")
      .param("estimated", "true"))
      .andExpect(status().isCreated());
  }

  @Test
  public void updateNode() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMYNODEDTO1);
    mockMvc.perform(put("/api/nodes/{id}", 1).with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isNoContent());
  }

  @Test
  public void deleteNode() throws Exception {
    mockMvc.perform(delete("/api/nodes/1").with(user("admin").roles("ADMIN")))
      .andExpect(status().isNoContent());
  }
}

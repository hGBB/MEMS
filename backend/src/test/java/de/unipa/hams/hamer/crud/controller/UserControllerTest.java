package de.unipa.hams.hamer.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.unipa.hams.hamer.crud.controller.dto.UserDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.UserMapper;
import de.unipa.hams.hamer.crud.service.api.UserCrudService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(secure = false)
public class UserControllerTest {

  private final static User DUMMY_USER = new User();
  private final static UserDTO DUMMY_USERDTO = new UserDTO();
  private final static User DUMMY_USER2 = new User();
  private final static UserDTO DUMMY_USERDTO2 = new UserDTO();
  private final static List<User> DUMMY_LIST = new LinkedList<>() {{
    add(DUMMY_USER);
    add(DUMMY_USER2);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserCrudService userService;

  @MockBean
  private UserMapper userMapper;

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
    UserDTO userDTO = new UserDTO();
    User user = new User();
    given(authorizationService.currentUser()).willReturn(user);
    given(userMapper.toDto(user)).willReturn(userDTO);
    given(userMapper.toDto(DUMMY_USER)).willReturn(DUMMY_USERDTO);
    given(userMapper.toEntity(DUMMY_USERDTO)).willReturn(DUMMY_USER);
    given(userMapper.toDto(DUMMY_USER2)).willReturn(DUMMY_USERDTO2);
    given(userService.getUsers()).willReturn(DUMMY_LIST);
    given(userService.getUser(1)).willReturn(DUMMY_USER);
    given(userService.createUser(DUMMY_USER, "hello")).willReturn(DUMMY_USER);
    given(userService.getUsers()).willReturn(DUMMY_LIST);
    //   given(userService.getUserFromAuthentication(null)).willReturn(DUMMY_USER);
    DUMMY_USERDTO.setChangedPassword("1234");
    DUMMY_USERDTO.setId(1L);
    DUMMY_USER.setId(1L);
  }

  @Test
  public void createUser() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_USERDTO);
    mockMvc.perform(post("/api/users").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isCreated());
  }

  @Test
  public void resetPassword() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_USERDTO);
    mockMvc.perform(post("/api/users/resetpwd").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isNoContent());
  }

  @Test
  public void newPassword() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_USERDTO);
    mockMvc.perform(post("/api/users/newpwd").with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson)
      .param("token", "1234"))
      .andExpect(status().isNoContent());
  }

  @Test
  public void getUser() throws Exception {
    mockMvc.perform(get("/api/users/1").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isNotEmpty())
      .andExpect(jsonPath("$").exists());
  }

  @Test
  public void getUsers() throws Exception {
    mockMvc.perform(get("/api/users").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty())
      .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  public void updateUser() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(DUMMY_USER);
    mockMvc.perform(put("/api/users/{id}", 1).with(user("admin").roles("ADMIN"))
      .contentType(MediaType.APPLICATION_JSON)
      .content(requestJson))
      .andExpect(status().isOk());
  }

  @Test
  public void deleteUser() throws Exception {
    mockMvc.perform(delete("/api/users/{id}", 1).with(user("admin").roles("ADMIN")))
      .andExpect(status().isNoContent());
  }
}

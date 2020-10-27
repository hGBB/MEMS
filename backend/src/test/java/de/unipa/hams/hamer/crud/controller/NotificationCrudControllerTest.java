package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.NotificationDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NotificationMapper;
import de.unipa.hams.hamer.crud.service.api.NotificationCrudService;
import de.unipa.hams.hamer.domain.Notification;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(NotificationCrudController.class)
@AutoConfigureMockMvc(secure = false)
public class NotificationCrudControllerTest {

  private static final Notification DUMMY_NOTIFICAITON = new Notification();
  private static final NotificationDTO DUMMY_NOTIFICAITONDTO = new NotificationDTO();
  private static final List<Notification> DUMMY_LIST = new LinkedList<>() {{
    add(DUMMY_NOTIFICAITON);
  }};

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NotificationCrudService notificationCrudService;

  @MockBean
  private NotificationMapper notificationMapper;

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
    given(notificationMapper.toDTO(DUMMY_NOTIFICAITON)).willReturn(DUMMY_NOTIFICAITONDTO);
    given(notificationCrudService.getNotificationById(1L)).willReturn(DUMMY_NOTIFICAITON);
    given(notificationCrudService.getAllNotifications()).willReturn(DUMMY_LIST);
    given(authorizationService.currentNotification(1L)).willReturn(DUMMY_NOTIFICAITON);
    given(authorizationService.currentNotifications()).willReturn(DUMMY_LIST);
  }

  @Test
  public void getNotification() throws Exception {
    mockMvc.perform(get("/api/notifications/1").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$").exists())
      .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  public void getNotifications() throws Exception {
    mockMvc.perform(get("/api/notifications").with(user("admin").roles("ADMIN"))
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)))
      .andExpect(jsonPath("$").isArray())
      .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  public void deleteNotification() throws Exception {
    mockMvc.perform(delete("/api/notifications/1").with(user("admin").roles("ADMIN")))
      .andExpect(status().isNoContent());
  }
}

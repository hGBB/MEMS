package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.NotificationCrudService;
import de.unipa.hams.hamer.domain.Notification;
import de.unipa.hams.hamer.persistence.api.NotificationRepository;
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
public class NotificationCrudServiceImplTest {

  private static final Notification N1 = new Notification();
  private static final Notification N2 = new Notification();
  private static final List<Notification> NL = new LinkedList<>() {{
    add(N1);
    add(N2);
  }};


  @Mock
  private static NotificationRepository notificationRepository;

  @Autowired
  private NotificationCrudService notificationCrudService;

  @Before
  public void setUp() throws Exception {
    given(notificationRepository.findAll()).willReturn(NL);
    given(notificationRepository.getOne(1L)).willReturn(N1);
    given(notificationRepository.findById(1L)).willReturn(java.util.Optional.of(N1));
  }

  @Test
  public void getAllNotification() {
    assertThat(notificationCrudService.getAllNotifications()).isSameAs(NL);
  }

  @Test
  public void getNotificationsById() {
    assertThat(notificationCrudService.getNotificationById(1L)).isEqualTo(N1);
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> notificationCrudService.getNotificationById(5L));
  }

  @Test
  public void deleteNotification() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> notificationCrudService.deleteNotification(5L));
  }

  @TestConfiguration
  static class NotificationCrudServiceBeanTestContextConfiguration {
    @Bean
    public NotificationCrudService notificationCrudService() {
      return new NotificationCrudServiceImpl(notificationRepository);
    }
  }
}

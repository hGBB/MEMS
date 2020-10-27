package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.controller.dto.mapper.UserMapper;
import de.unipa.hams.hamer.crud.service.api.AlertCrudService;
import de.unipa.hams.hamer.crud.service.api.HamerMailSenderService;
import de.unipa.hams.hamer.crud.service.api.UserCrudService;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import eu.fraho.spring.securityJwt.base.dto.JwtUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.ignoreStubs;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

  private static final User U1 = new User();
  private static final User U2 = new User();
  private static final JwtUser JWT = new JwtUser();
  private static final List<User> UL = new LinkedList<>() {{
    add(U1);
    add(U2);
  }};

  @Autowired
  private UserCrudService userService;

  @Mock
  private static UserRepository userRepository;

  @Mock
  private static UserMapper userMapper;
  @Mock
  private static PasswordEncoder passwordEncoder;

  @Mock
  private static HamerMailSenderService mailSender;

  @Mock
  private static AuthorityService authorityService;

  @Mock
  private static AuthorizationService authorizationService;

  @Mock
  private static AlertCrudService alertCrudService;

//  @MockBean
//  private static GroupCrudService groupCrudService;
//
//  @MockBean
//  private static NotificationCrudService notificationCrudService;
//
//  @MockBean
//  private static RuleDesignationService ruleDesignationService;

  @Before
  public void setUp() throws Exception {
    U2.setLogin("test");
    U2.setPassword("1234");
    U2.setPrivileged(true);
    U2.setAdmin(true);
    U2.setId(2L);
    JWT.setUsername("test");
    JWT.setPassword("1234");
    JWT.setAccountNonExpired(true);
    JWT.setAuthorities(U2.getAuthorities());
    JWT.setId(2L);
    JWT.setEnabled(true);
    JWT.setCredentialsNonExpired(true);
    JWT.setApiAccessAllowed(true);
    JWT.setAccountNonLocked(true);
    given(userRepository.findAll()).willReturn(UL);
    given(userRepository.findById(1L)).willReturn(java.util.Optional.of(U1));
    given(userRepository.getOne(1L)).willReturn(U1);
    given(userRepository.getOne(2L)).willReturn(U2);
    U1.setEmail("Works");
    given(userRepository.findByLogin("fail")).willReturn(null);
    given(userRepository.findByLogin("success")).willReturn(U1);
    given(passwordEncoder.encode("1234")).willReturn("ladida");
    given(userRepository.save(U1)).willReturn(U1);
    given(authorityService.hasRole("ROLE_ADMIN")).willReturn(true);
    ignoreStubs(mailSender);

  }

  @Test
  public void getUsers() {
    assertThat(userService.getUsers()).isEqualTo(UL);
  }

  @Test
  public void getUser() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> userService.getUser(2L));
    assertThat(userService.getUser(1L)).isEqualTo(U1);
  }

  @Test
  public void getUserFromAuthentication() {
  }

  @Test
  public void resetPassword() {
    assertThatExceptionOfType(AccessDeniedException.class)
      .isThrownBy(() -> userService.resetPassword("fail", "email"))
      .withMessage("We do not have this user!");
    assertThatExceptionOfType(AccessDeniedException.class)
      .isThrownBy(() -> userService.resetPassword("success", "email"))
      .withMessage("Wrong Email!");
  }

  @Test
  public void newPassword() {
    assertThatExceptionOfType(AccessDeniedException.class)
      .isThrownBy(() -> userService.newPassword("fail", "email"))
      .withMessage("Invalid password reset.");
  }

  @Test
  public void createUser() throws MessagingException {
    assertThat(userService.createUser(U1, "hello")).isEqualTo(U1);
  }

  @Test
  public void updateUser() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> userService.deleteUser(5));
  }

  @Test
  public void deleteUser() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> userService.deleteUser(5));
  }

  @TestConfiguration
  static class UserServiceBeanTestContextConfiguration {
    @Bean
    public UserCrudService userService() {
      return new UserCrudServiceImpl(userRepository, userMapper, passwordEncoder, mailSender, authorityService, authorizationService, alertCrudService, null, null, null);
    }
  }

}


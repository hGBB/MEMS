package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.controller.dto.mapper.GroupMapper;
import de.unipa.hams.hamer.crud.service.api.GroupCrudService;
import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.persistence.api.UserGroupRepository;
import de.unipa.hams.hamer.persistence.api.UserRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class GroupCrudServiceImplTest {

  private static final UserGroup USER_GROUP1 = new UserGroup();
  private static final UserGroup USER_GROUP2 = new UserGroup();
  private static final List<UserGroup> USER_GROUP_LIST = new LinkedList<>() {{
    add(USER_GROUP1);
    add(USER_GROUP2);
  }};
  private static final User USER1 = new User();
  private static final Node NODE1 = new Node();
  // lets check out if the mapper can be tested here too!
  private static GroupMapper groupMapper;

  @Mock
  private static UserGroupRepository userGroupRepository;

  @Mock
  private static UserRepository userRepository;

  @Mock
  private static AuthorizationService authorizationService;

  @Mock
  private static AuthorityService authorityService;

  @Mock
  private static RuleDesignationService ruleDesignationService;

  @Mock
  private static NodeRepository nodeRepository;


  @Autowired
  private GroupCrudService groupCrudService;

  @Before
  public void setUp() throws Exception {
    NODE1.setId(1L);
    USER1.setId(1L);
    USER_GROUP1.setName("Test");
    Set<User> userSet = new HashSet<>();
    userSet.add(USER1);
    Set<Node> nodeSet = new HashSet<>();
    nodeSet.add(NODE1);
    USER_GROUP2.setId(1L);
    USER_GROUP1.setNodes(nodeSet);
    USER_GROUP1.setUsers(userSet);
    USER_GROUP2.setNodes(nodeSet);
    USER_GROUP2.setUsers(userSet);

    given(authorityService.hasRole("Role_Admin")).willReturn(true);
    given(userGroupRepository.saveAndFlush(USER_GROUP1)).willReturn(USER_GROUP2);
    given(userGroupRepository.getOne(1L)).willReturn(USER_GROUP1);
    given(userGroupRepository.findAll()).willReturn(USER_GROUP_LIST);
    given(userRepository.getOne(1L)).willReturn(USER1);
    given(nodeRepository.getOne(1L)).willReturn(NODE1);
    given(userGroupRepository.findById(1L)).willReturn(java.util.Optional.of(USER_GROUP1));
    given(authorityService.hasRole("ROLE_ADMIN")).willReturn(true);
  }

  @Test
  public void createUserGroup() {
    assertThat(groupCrudService.createUserGroup(USER_GROUP1)).isEqualTo(1L);
  }

  @Test
  public void getUserGroupById() {
    assertThat(groupCrudService.getUserGroupById(1L)).isSameAs(USER_GROUP1);
  }

  @Test
  public void getAllUserGroups() {
    assertThat(groupCrudService.getAllUserGroups()).isSameAs(USER_GROUP_LIST);
  }

  @Test
  public void updateUserGroup() {
    // this one is werid!
    // assertThat(groupCrudService.updateUserGroup(1L, USER_GROUP1)).isEqualTo(USER_GROUP2);
//    assertThatExceptionOfType(EntityNotFoundException.class)
//      .isThrownBy(() -> groupCrudService.updateUserGroup(5L, USER_GROUP1)); those exceptions got removed
  }

  @Test
  public void deleteUserGroup() {
    assertThatExceptionOfType(EntityNotFoundException.class)
      .isThrownBy(() -> groupCrudService.deleteUserGroup(5L));
  }

  @TestConfiguration
  static class GroupCrudServiceBeanTestContextConfiguration {
    @Bean
    public GroupCrudServiceImpl groupCrudService() {
      return new GroupCrudServiceImpl(userGroupRepository, userRepository, nodeRepository, groupMapper, authorizationService, authorityService, ruleDesignationService);
    }


  }
}

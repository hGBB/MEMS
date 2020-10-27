package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class NodeCrudServiceImplTest {

  private static final Node N_1 = new Node();
  @Mock
  private static NodeRepository nodeRepository;
  @Autowired
  private NodeCrudService nodeCrudService;

  @Before
  public void setUp() throws Exception {
    N_1.setId(1L);
    N_1.setName("Node 1");
    N_1.setSendInterval(42);
    List<Node> nodes = new LinkedList<>();
    nodes.add(N_1);
    given(nodeRepository.findAll()).willReturn(nodes);
  }

  @Test
  public void getNodes() {
    assertThat(nodeCrudService.getNodes().size()).isEqualTo(1);
    assertThat(nodeCrudService.getNodes()).contains(N_1);
  }

  @TestConfiguration
  static class NodeCrudServiceBeanTestContextConfiguration {
    @Bean
    public NodeCrudService nodeCrudService() {
      return new NodeCrudServiceImpl(nodeRepository, null, null, null, null, null, null);
    }
  }
}

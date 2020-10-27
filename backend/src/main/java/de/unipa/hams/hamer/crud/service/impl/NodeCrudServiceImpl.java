package de.unipa.hams.hamer.crud.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.domain.NodePin;
import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.crypt.CryptUtil;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.event.NodeAdded;
import de.unipa.hams.hamer.event.NodeDeleted;
import de.unipa.hams.hamer.exception.McRegistrationException;
import de.unipa.hams.hamer.exception.NodeDeleteException;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import de.unipa.hams.hamer.security.service.api.AuthorityService;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NodeCrudServiceImpl implements NodeCrudService {

  private final NodeRepository nodeRepository;
  private final PasswordEncoder passwordEncoder;
  private final NodeMapper nodeMapper;
  private final AuthorizationService authorizationService;
  private final ApplicationEventPublisher eventPublisher;
  private final AuthorityService authorityService;
  private final RecordRepository recordRepository;

  @Value("${mems.monitoring.defaultSendInterval:5}")
  private int defaultSendInterval;
  @Value("${mems.monitoring.registration.pin.validity}")
  private int pinValiditySeconds;

  private Cache<String, String> pins = CacheBuilder.newBuilder().expireAfterWrite(pinValiditySeconds + 1, TimeUnit.MINUTES).build();
  private Map<Long, Node> configurations = new HashMap<>();

  @Override
  @NotNull
  public List<Node> getNodes() {
    return nodeRepository.findAll();
  }

  @Override
  public @NotNull Node getNode(@NotNull Long id) {
    return nodeRepository.getOne(id);
  }

  @Override
  public @NotNull Node registerNode(@NotNull String key, @NotNull String name, boolean estimated) {
    if (pins.asMap().containsValue(key)) {
      Node n = new Node();
      n.setName(name);
      n.setSendInterval(defaultSendInterval);
      n.setEstimated(estimated);
      UUID u = UUID.randomUUID();
      n.setUuid(passwordEncoder.encode(u.toString()));
      n = nodeRepository.saveAndFlush(n);
      eventPublisher.publishEvent(new NodeAdded(this, n));
      n.setUuid(u.toString());
      return n;
    } else {
      throw new McRegistrationException("The registration PIN is not correct. Refused to register Monitoring Client.");
    }
  }

  @Override
  @PreAuthorize("hasRole('ROLE_NODE')")
  public @Nullable Node getChangedConfiguration() {
    var nodeId = authorizationService.currentNode();
    var res = configurations.get(nodeId);
    configurations.remove(nodeId);
    return res;
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public NodePin getPin() {
    NodePin pin = new NodePin(CryptUtil.fourDigitPin(), pinValiditySeconds);
    pins.put(UUID.randomUUID().toString(), pin.getValue());
    return pin;
  }

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @Transactional
  public void deleteNode(@NotNull Long id) {
    Node n = nodeRepository.getOne(id);
    if (!n.getGroups().isEmpty()) {
      throw new NodeDeleteException("The node cannot be deleted as long as it is assigned to groups.");
    }
    recordRepository.deleteAllBySentBy(n);
    nodeRepository.delete(n);
    eventPublisher.publishEvent(new NodeDeleted(this, id));
    n.setRegistered(false);
    configurations.put(n.getId(), n);
  }

  @Override
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public void updateNode(@NotNull Long id, Node node) {
    if (authorityService.hasRole("ROLE_PRIVILEGED") && !authorityService.hasAccessToNode(node)) {
      throw new AccessDeniedException("You have no access to this node");
    }
    var n = nodeRepository.getOne(id);
    nodeMapper.merge(node, n);
    nodeRepository.save(n);
    configurations.put(n.getId(), n);
  }

  @Override
  public void updateNodeExternal(@NotNull Node node) {
    configurations.put(node.getId(), node);
  }
}

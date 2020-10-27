package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.NodeDTO;
import de.unipa.hams.hamer.crud.controller.dto.NodePinDTO;
import de.unipa.hams.hamer.crud.controller.dto.NodeRegistrationDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodePinMapper;
import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NodeCrudController {

  private final AuthorizationService authorizationService;

  private final NodeCrudService nodeCrudService;

  private final NodeMapper nodeMapper;

  private final NodePinMapper nodePinMapper;

  @GetMapping("/api/nodes")
  @NotNull
  public List<NodeDTO> getNodes() {
    List<Node> nodes = authorizationService.currentNodes();
    return nodes.stream().map(nodeMapper::entityToDTO).collect(Collectors.toList());
    //return nodeCrudService.getNodes().stream().map(nodeMapper::entityToDTO).collect(Collectors.toList());
  }

  @GetMapping(value = "/api/nodes/pin")
  @NotNull
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public NodePinDTO requestRegistrationPin() {
    return nodePinMapper.toDto(nodeCrudService.getPin());
  }

  @GetMapping("/api/nodes/configuration")
  @PreAuthorize("hasRole('ROLE_NODE')")
  public NodeDTO configuration() {
    Node n = nodeCrudService.getChangedConfiguration();
    if (n == null) return null;
    return nodeMapper.entityToDTO(n);
  }

  @PostMapping(value = "/api/nodes", params = {"key", "name"})
  @ResponseStatus(HttpStatus.CREATED)
  @NotNull
  @PreAuthorize("isAnonymous()")
  public NodeRegistrationDTO registerNode(@RequestParam("key") @NotNull String apiKey, @RequestParam("name") @NotNull String name, @RequestParam("estimated") boolean estimated) {
    Node n = nodeCrudService.registerNode(apiKey, name, estimated);
    return new NodeRegistrationDTO(n.getId(), n.getUuid());
  }

  @PutMapping("/api/nodes/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public void updateNode(@PathVariable("id") Long id, @RequestBody NodeDTO nodeDTO) {
    nodeCrudService.updateNode(id, nodeMapper.dtoToEntity(nodeDTO));
  }

  @DeleteMapping("/api/nodes/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteNode(@PathVariable("id") @NotNull Long id) {
    nodeCrudService.deleteNode(id);
  }

}

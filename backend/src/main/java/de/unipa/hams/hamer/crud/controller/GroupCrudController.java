package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.UserGroupDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.GroupMapper;
import de.unipa.hams.hamer.crud.service.api.GroupCrudService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupCrudController {

  private GroupCrudService groupCrudService;
  private GroupMapper groupMapper;

  @NotNull
  @PostMapping("/groups")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createGroup(@NotNull @RequestBody UserGroupDTO group, UriComponentsBuilder b) {
    Long id = groupCrudService.createUserGroup(groupMapper.toEntity(group));
    UriComponents uric = b.path("/api/groups/{id}").buildAndExpand(id);
    return ResponseEntity.created(uric.toUri()).build();
  }

  @NotNull
  @GetMapping("/groups")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public List<UserGroupDTO> getGroups(Authentication a) {
    return groupCrudService.getAllUserGroups().stream().map(g -> groupMapper.toDto(g)).collect(Collectors.toList());
  }

  @NotNull
  @GetMapping("/groups/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public UserGroupDTO getGroup(@NotNull @PathVariable("id") long id) {
    return groupMapper.toDto(groupCrudService.getUserGroupById(id));
  }

  @NotNull
  @PutMapping("/groups/{id}")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public void updateGroup(@NotNull @PathVariable("id") long id, @RequestBody UserGroupDTO group) {
    groupCrudService.updateUserGroup(id, groupMapper.toEntity(group));
  }

  @NotNull
  @DeleteMapping(value = "/groups/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PRIVILEGED')")
  public void deleteGroup(@NotNull @PathVariable("id") long id) {
    groupCrudService.deleteUserGroup(id);
  }
}

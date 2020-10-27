package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.UserDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.UserMapper;
import de.unipa.hams.hamer.crud.service.api.UserCrudService;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.LinkedList;
import java.util.List;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

  private UserCrudService userCrudService;
  private AuthorizationService authorizationService;
  private UserMapper userMapper;

  @PostMapping("/api/users")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO, UriComponentsBuilder ucb) throws MessagingException {
//    if (userDTO.getChangedPassword() == null) {
//      throw new IllegalArgumentException("New User needs a password!");
//    }
    User u = userCrudService.createUser(userMapper.toEntity(userDTO), userDTO.getChangedPassword());
    UriComponents uri = ucb.buildAndExpand("/api/users/{id}", u.getId());
    return ResponseEntity.created(uri.toUri()).build();
  }

  @PostMapping("/api/users/resetpwd")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetPassword(@RequestBody UserDTO userDTO) throws MessagingException {
    userCrudService.resetPassword(userDTO.getLogin(), userDTO.getEmail());
  }

  @PostMapping(value = "/api/users/newpwd", params = "token")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("permitAll()")
  public void newPassword(@RequestParam("token") @NotNull String token, @RequestBody UserDTO userDTO) {
    userCrudService.newPassword(token, userDTO.getChangedPassword());
  }

  @NotNull
  @GetMapping("/api/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDTO getUser(@PathVariable("id") long id) {
    return userMapper.toDto(userCrudService.getUser(id));
  }

  @NotNull
  @GetMapping("/api/users")
  @ResponseStatus(HttpStatus.OK)
  public List<UserDTO> getUsers() {
    List<UserDTO> userDTOS = new LinkedList<>();
    for (User u : userCrudService.getUsers()) {
      userDTOS.add(userMapper.toDto(u));
    }
    return userDTOS;
  }

  @GetMapping("/api/users/self")
  @ResponseStatus(HttpStatus.OK)
  public UserDTO getOwnPrincipal() throws AuthorizationContextException {
    return userMapper.toDto(authorizationService.currentUser());
  }

  @PutMapping("/api/users/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updateUser(@PathVariable("id") long id, @RequestBody UserDTO userDTO) {
    userCrudService.updateUser(id, userMapper.toEntity(userDTO), userDTO.getChangedPassword(), userDTO.getOldPassword());
  }

  @DeleteMapping("api/users/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("id") long id) {
    userCrudService.deleteUser(id);
  }

}

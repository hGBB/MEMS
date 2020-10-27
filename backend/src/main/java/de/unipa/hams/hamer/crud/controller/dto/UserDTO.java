package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
  private Long id;
  private String login;
  private String changedPassword;
  private String oldPassword;
  private String email;
  private String name;
  private boolean admin;
  private boolean privileged;
}

package de.unipa.hams.hamer.crud.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserGroupDTO {
  private long id;
  private String name;
  private List<UserDTO> users;
  private List<NodeDTO> nodes;
}

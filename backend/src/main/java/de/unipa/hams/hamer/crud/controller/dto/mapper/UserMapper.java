package de.unipa.hams.hamer.crud.controller.dto.mapper;

import de.unipa.hams.hamer.crud.controller.dto.UserDTO;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @NotNull
  @Mapping(target = "password", ignore = true) // Never map password!!!!!!!
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "groups", ignore = true)
  public abstract User toEntity(UserDTO user);

  @NotNull
  @Mapping(target = "changedPassword", ignore = true) // Same!
  public abstract UserDTO toDto(User user);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "password", ignore = true)
  public abstract void mergeDomain(User source, @MappingTarget User target);


  // Password hashing takes place in service via PasswordEncryptor
}

package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
  @NotNull
  List<UserGroup> findAllByUsersContaining(User user);
}

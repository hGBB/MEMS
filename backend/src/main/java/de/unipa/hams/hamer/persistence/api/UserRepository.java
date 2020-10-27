package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByLogin(String login);

  List<User> findAllByGroupsContaining(List<UserGroup> groups);

  List<User> findAllByAdminIs(boolean isAdmin);
}

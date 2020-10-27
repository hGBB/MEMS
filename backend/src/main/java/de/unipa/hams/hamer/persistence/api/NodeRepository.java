package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.Node;
import de.unipa.hams.hamer.domain.UserGroup;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NodeRepository extends JpaRepository<Node, Long> {

  Node findByName(@NotNull @Param("name") String name);

  Node findByUuid(@NotNull String uuid);

  @NotNull
  List<Node> findAllByIdNotIn(@NotNull List<Long> ids);

  @NotNull
  List<Node> findAllByGroups(@NotNull List<UserGroup> groups);

}

package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.crud.domain.NodePin;
import de.unipa.hams.hamer.domain.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NodeCrudService {
  @NotNull
  List<Node> getNodes();

  @NotNull
  Node getNode(@NotNull Long id);

  @NotNull
  Node registerNode(@NotNull String key, @NotNull String name, boolean estimated);

  NodePin getPin();

  void deleteNode(@NotNull Long id);

  void updateNode(@NotNull Long id, Node node);

  void updateNodeExternal(@NotNull Node node);

  @Nullable
  Node getChangedConfiguration();
}

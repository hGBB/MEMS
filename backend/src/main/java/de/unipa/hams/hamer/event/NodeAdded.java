package de.unipa.hams.hamer.event;

import de.unipa.hams.hamer.domain.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NodeAdded extends ApplicationEvent {

  private Node node;

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public NodeAdded(Object source, Node node) {
    super(source);
    this.node = node;
  }
}

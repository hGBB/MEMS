package de.unipa.hams.hamer.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class NodeDeleted extends ApplicationEvent {
  private Long id;

  public NodeDeleted(Object source, Long id) {
    super(source);
    this.id = id;
  }
}

package de.unipa.hams.hamer.event;

import de.unipa.hams.hamer.domain.Record;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

@Data
@EqualsAndHashCode(callSuper = true)
public class RecordReceived extends ApplicationEvent {
  private Record record;

  public RecordReceived(Object source, Record record) {
    super(source);
    this.record = record;
  }
}

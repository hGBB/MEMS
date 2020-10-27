package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.domain.Record;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface RecordCrudService {
  long createRecord(@NotNull Record record);

  Record getByTimestampAndType(@NotNull Date date, @NotNull Kpi type);
}

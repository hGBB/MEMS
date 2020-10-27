package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.RecordCrudService;
import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.persistence.api.RecordRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RecordCrudServiceImpl implements RecordCrudService {

  private RecordRepository recordRepository;

  @Override
  public long createRecord(@NotNull Record record) {
    return recordRepository.save(record).getId();
  }

  @Override
  public Record getByTimestampAndType(@NotNull Date date, @NotNull Kpi type) {
    return recordRepository.getByTimestampAndType(date, type);
  }

}

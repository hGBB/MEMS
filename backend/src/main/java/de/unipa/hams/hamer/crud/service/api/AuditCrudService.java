package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.Audit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface AuditCrudService {

  long createAudit(Audit audit);

  @NotNull
  List<Audit> getAllAudits();

}

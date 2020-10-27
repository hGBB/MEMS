package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.AuditCrudService;
import de.unipa.hams.hamer.domain.Audit;
import de.unipa.hams.hamer.persistence.api.AuditRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditCrudServiceImpl implements AuditCrudService {

  private AuditRepository auditRepository;

  @Override
  public long createAudit(Audit audit) {
    return auditRepository.saveAndFlush(audit).getId();
  }

  @Override
  public @NotNull List<Audit> getAllAudits() {
    return auditRepository.findAll();
  }
}

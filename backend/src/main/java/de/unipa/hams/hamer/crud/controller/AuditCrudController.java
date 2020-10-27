package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.AuditDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.AuditMapper;
import de.unipa.hams.hamer.crud.service.api.AuditCrudService;
import de.unipa.hams.hamer.domain.Audit;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuditCrudController {

  private AuditCrudService auditCrudService;
  private AuditMapper auditMapper;

  @NotNull
  @GetMapping("/audit")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<AuditDTO> getAudit() {
    List<AuditDTO> result = new LinkedList<>();
    for (Audit a : auditCrudService.getAllAudits()) {
      result.add(auditMapper.toDTO(a));
    }

    return result;
  }

}

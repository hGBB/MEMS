package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.KpiDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.KpiMapper;
import de.unipa.hams.hamer.crud.service.api.KpiCrudService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KpiCrudController {

  private KpiCrudService kpiCrudService;
  private KpiMapper kpiMapper;

  @NotNull
  @GetMapping("/api/kpi")
  @ResponseStatus(HttpStatus.OK)
  public List<KpiDTO> getKpis() {
    return kpiCrudService.getKpis().stream().map(kpiMapper::toDto).collect(Collectors.toList());
  }
}

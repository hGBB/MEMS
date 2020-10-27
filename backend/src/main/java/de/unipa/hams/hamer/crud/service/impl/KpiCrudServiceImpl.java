package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.KpiCrudService;
import de.unipa.hams.hamer.domain.Kpi;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class KpiCrudServiceImpl implements KpiCrudService {

  private KpiRepository kpiRepository;

  @Override
  @NotNull
  public List<Kpi> getKpis() {
    return kpiRepository.findAll();
  }

  @Override
  public @NotNull Kpi getByKpiName(String name) {
    return kpiRepository.findByName(name);
  }
}

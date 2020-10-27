package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.Kpi;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface KpiCrudService {

  @NotNull
  List<Kpi> getKpis();

  @NotNull
  Kpi getByKpiName(String name);
}

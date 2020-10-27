package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.Kpi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Null;

public interface KpiRepository extends JpaRepository<Kpi, Long> {

  @NotNull
  Kpi findByName(@NotNull @Param("name") String name);
}

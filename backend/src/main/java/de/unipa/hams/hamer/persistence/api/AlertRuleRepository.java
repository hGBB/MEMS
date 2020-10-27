package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.AlertRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The Repository is responsible for everything database related.
 * lucky me because spring is able to handle most of those calls by default
 */
public interface AlertRuleRepository extends JpaRepository<AlertRule, Long> {

  @Nullable
  AlertRule findAlertRuleByName(@NotNull @Param("name") String name);

  List<AlertRule> findAllByConfiguredBy_Id(@Param("userId") long id);

  void deleteAllByConfiguredById(@NotNull Long userId);
}

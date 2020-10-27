package de.unipa.hams.hamer.persistence.api;

import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.RuleDesignationKey;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleDesignationRepository extends JpaRepository<RuleDesignation, RuleDesignationKey> {

  void deleteAllById_RuleConfiguredBy(@NotNull User user);
}

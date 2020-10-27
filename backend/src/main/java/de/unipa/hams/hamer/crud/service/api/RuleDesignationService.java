package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.RuleDesignationKey;
import de.unipa.hams.hamer.domain.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RuleDesignationService {

  void createRuleDesignation(@NotNull RuleDesignation ruleDesignation);

  boolean findByIdExists(RuleDesignationKey id);

  void updateRuleDesignation(RuleDesignationKey id, double threshold, boolean leq);

  @NotNull
  List<RuleDesignation> getAll();

  void deleteRuleDesignation(RuleDesignationKey id);

  void deleteAllRuleDesignationsByUser(@NotNull User user);
}

package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.RuleDesignationKey;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.persistence.api.RuleDesignationRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDesignationServiceImpl implements RuleDesignationService {

  private RuleDesignationRepository ruleDesignationRepository;

  @Override
  public void createRuleDesignation(@NotNull RuleDesignation ruleDesignation) {
    // create if no id is found create new ruleset
    if (ruleDesignationRepository.findById(ruleDesignation.getId()).isEmpty()) {
      ruleDesignationRepository.saveAndFlush(ruleDesignation);
    } else {
      ruleDesignationRepository.deleteById(ruleDesignation.getId());
      ruleDesignationRepository.saveAndFlush(ruleDesignation);
    }
  }

  @Override
  public boolean findByIdExists(RuleDesignationKey id) {
    return ruleDesignationRepository.findById(id).isPresent();
  }

  @Override
  public void updateRuleDesignation(RuleDesignationKey id, double threshold, boolean leq) {
    if (ruleDesignationRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException();
    } else {
      RuleDesignation rd = ruleDesignationRepository.getOne(id);
      rd.setLowerOrEquals(leq);
      rd.setThreshold(threshold);
      ruleDesignationRepository.saveAndFlush(rd);
    }
  }

  @Override
  public @NotNull List<RuleDesignation> getAll() {
    return ruleDesignationRepository.findAll();
  }

  @Override
  public void deleteRuleDesignation(RuleDesignationKey id) {
    if (ruleDesignationRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException();
    } else {
      ruleDesignationRepository.deleteById(id);
    }
  }

  @Override
  public void deleteAllRuleDesignationsByUser(@NotNull User user) {
    ruleDesignationRepository.deleteAllById_RuleConfiguredBy(user);
  }
}

package de.unipa.hams.hamer.crud.service.impl;

import de.unipa.hams.hamer.crud.service.api.AlertCrudService;
import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.persistence.api.AlertRuleRepository;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * {@inheritDoc}
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AlertCrudServiceImpl implements AlertCrudService {

  private AlertRuleRepository alertRuleRepository;
  private AuthorizationService authorizationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public long createAlertRule(@NotNull AlertRule alertRule) {
    configureUser(alertRule);
    return alertRuleRepository.saveAndFlush(alertRule).getId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public @NotNull AlertRule getAlertRuleById(long id) {
    if (alertRuleRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException("Alert Rule with the ID " + id + " does not exist!");
    }
    // Check if it exists!
    // Define Exception, throw it, test it :)
    // ...And catch it in RestExceptionHanlder
    return alertRuleRepository.getOne(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public @NotNull List<AlertRule> getAllAlertRules(long id) {

    return alertRuleRepository.findAllByConfiguredBy_Id(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateAlertRule(long id, AlertRule alertRule) {
    if (alertRuleRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException("Alert Rule with the ID " + id + " does not exist!");
    }
    configureUser(alertRule);
    alertRule.setId(id);
    alertRuleRepository.saveAndFlush(alertRule);
  }

  private void configureUser(AlertRule alertRule) {
    User user = null;
    try {
      user = authorizationService.currentUser();
    } catch (AuthorizationContextException e) {
      log.error(ExceptionUtils.getStackTrace(e));
    }
    alertRule.setConfiguredBy(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteAlertRule(long id) {
    // Due to testability it is never suitable to rely on bubbling.
    if (alertRuleRepository.findById(id).isEmpty()) {
      throw new EntityNotFoundException("Alert Rule with ID " + id + " does not exist!");
    }
    alertRuleRepository.deleteById(id);
  }

}

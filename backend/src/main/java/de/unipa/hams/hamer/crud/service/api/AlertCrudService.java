package de.unipa.hams.hamer.crud.service.api;

import de.unipa.hams.hamer.domain.AlertRule;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Service for Alert Rules, the business logic is embedded here.
 * The Service is called by the Controller
 */
public interface AlertCrudService {

  /**
   * Creates a new Alert Rule
   *
   * @param alertRule Alert Rule Object
   * @return The assigned id
   */
  long createAlertRule(@NotNull AlertRule alertRule);

  /**
   * Get Alert Rule by id
   *
   * @param id Alert Rule identifying id
   * @return Alert Rule Object
   */
  @NotNull
  AlertRule getAlertRuleById(long id);

  /**
   * Get all Alert Rules
   *
   * @return List of all Alert Rules in the System
   */
  @NotNull
  List<AlertRule> getAllAlertRules(long id);

  /**
   * Update an existing Alert Rule
   *
   * @param id Alert Rule identifying id
   * @param alertRule Updated Alert Rule Object
   */
  void updateAlertRule(long id, AlertRule alertRule);

  /**
   * Delete an existing Alert Rule
   *
   * @param id Alert Rule identifying id
   */
  void deleteAlertRule(long id);

}

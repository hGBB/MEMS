package de.unipa.hams.hamer.crud.controller;

import de.unipa.hams.hamer.crud.controller.dto.AlertKpiDTO;
import de.unipa.hams.hamer.crud.controller.dto.AlertRuleDTO;
import de.unipa.hams.hamer.crud.controller.dto.NodeDTO;
import de.unipa.hams.hamer.crud.controller.dto.mapper.AlertMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.KpiMapper;
import de.unipa.hams.hamer.crud.controller.dto.mapper.NodeMapper;
import de.unipa.hams.hamer.crud.service.api.AlertCrudService;
import de.unipa.hams.hamer.crud.service.api.RuleDesignationService;
import de.unipa.hams.hamer.domain.AlertRule;
import de.unipa.hams.hamer.domain.RuleDesignation;
import de.unipa.hams.hamer.domain.RuleDesignationKey;
import de.unipa.hams.hamer.domain.User;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * API to access Alert Rules from the Backend Server implementing all CRUD
 * Functions
 * Entities are mapped to DTOs and vice versa here
 */
@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AlertCrudController {

  private AuthorizationService authorizationService;
  private AlertCrudService alertCrudService;
  private RuleDesignationService ruleDesignationService;
  private AlertMapper alertMapper;
  private NodeMapper nodeMapper;
  private KpiMapper kpiMapper;

  /**
   * POST API for Alert Rules
   *
   * @param alert an Alert Rule Object
   * @param b     UriComponentsBuilder
   * @return ResponseEntity with URI of new Alert Rule
   */
  @NotNull
  @PostMapping("/alerts")
  @ResponseStatus(HttpStatus.CREATED) // Set status
  public ResponseEntity<?> createAlert(@NotNull @RequestBody AlertRuleDTO alert, UriComponentsBuilder b) {
    // create ar so we have a usable id to create RULE DESIGNATIONS
    Long id = alertCrudService.createAlertRule(alertMapper.toEntity(alert));
    // for each node create a set of kpis
    createRuleDesignations(alert);
    UriComponents uric = b.path("/api/alerts/{id}").buildAndExpand(id);
    return ResponseEntity.created(uric.toUri()).build();
  }

  /**
   * GET API for Alert Rules
   *
   * @return All Alert Rules in the System
   */
  @NotNull
  @GetMapping("/alerts")
  @ResponseStatus(HttpStatus.OK)
  public List<AlertRuleDTO> getAlerts() {
    User user = authorizationService.currentUser();
    List<AlertRuleDTO> alertRuleDTOS = new LinkedList<>();
    for (AlertRule ar : alertCrudService.getAllAlertRules(user.getId())) {
      AlertRuleDTO addDTO = alertMapper.toDto(ar);
      addNodesAndKpisToDTO(addDTO);
      alertRuleDTOS.add(addDTO);
    }
    return alertRuleDTOS;
  }

  /**
   * GET API for certain Alert Rule
   *
   * @param id Alert Rule identifying id
   * @return Alert Rule ObjectDTO //
   */
  @NotNull
  @GetMapping("/alerts/{id}")
  @ResponseStatus(HttpStatus.OK)
  // PathVariable, not RequestParam!
  public AlertRuleDTO getAlert(@NotNull @PathVariable("id") long id) {
    User user = authorizationService.currentUser();
    AlertRule alertRule = alertCrudService.getAlertRuleById(id);
    if (alertRule.getConfiguredBy() != user) {
      throw new AuthorizationContextException("You can't access Alert Rules that were not created by you!");
    } else {
      AlertRuleDTO ar = alertMapper.toDto(alertRule);
      addNodesAndKpisToDTO(ar);
      return ar;
    }

    // get rd for picked alertRule and transform them to dtos
    // add the kpis to the alertruledto
  }

  private void addNodesAndKpisToDTO(AlertRuleDTO ar) {
    List<NodeDTO> nodeDTOS = new LinkedList<>();
    List<AlertKpiDTO> kpis = new LinkedList<>();
    List<RuleDesignation> rd = ruleDesignationService.getAll();
    for (RuleDesignation r : rd) {
      if (r.getRule().getId() == ar.getId() && !nodeDTOS.contains(nodeMapper.entityToDTO(r.getNode()))) {
        nodeDTOS.add(nodeMapper.entityToDTO(r.getNode()));
      }
      if (r.getRule().getId() == ar.getId()) {
        AlertKpiDTO addKpi = new AlertKpiDTO();
        addKpi.setKpiDTO(kpiMapper.toDto(r.getKpi()));
        addKpi.setThreshold(r.getThreshold());
        addKpi.setLeq(r.isLowerOrEquals());
        boolean add = true;
        for (AlertKpiDTO ak : kpis) {
          if (addKpi.getKpiDTO().equals(ak.getKpiDTO())) {
            add = false;
            break;
          }
        }
        if (add) {
          kpis.add(addKpi);
        }
      }
    }
    ar.setNodes(nodeDTOS);
    ar.setKpis(kpis);
  }

  /**
   * PUT API for Alert Rules
   * <p>
   * \ /
   * oVo
   * \___XXX___/
   * __XXXXX__
   * /__XXXXX__\
   * /   XXX   \
   * V
   * <p>
   * oh no you found a bug!
   *
   * @param id    Alert Rule identifying id
   * @param alert updated Alert Rule Object
   */
  @NotNull
  @PutMapping("/alerts/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updateAlert(@NotNull @PathVariable("id") long id, @RequestBody AlertRuleDTO alert) {
    createRuleDesignations(alert);
    alertCrudService.updateAlertRule(id, alertMapper.toEntity(alert));
  }

  private void createRuleDesignations(@RequestBody AlertRuleDTO alert) {
    // first remove all rule designations
    checkForDeletableRuleDesignationsAndDelete(alert);
    if (alert.getNodes() != null) {
      for (NodeDTO n : alert.getNodes()) {
        for (AlertKpiDTO a : alert.getKpis()) {
          RuleDesignationKey key = getRuleDesignationKey(alert, n, a);
          if (!ruleDesignationService.findByIdExists(key)) {
            RuleDesignation ruleDesignation = new RuleDesignation();
            ruleDesignation.setThreshold(a.getThreshold());
            ruleDesignation.setLowerOrEquals(a.isLeq());
            ruleDesignation.setId(key);
            ruleDesignationService.createRuleDesignation(ruleDesignation);
          } else {
            ruleDesignationService.updateRuleDesignation(key, a.getThreshold(), a.isLeq());
          }
        }
      }
    }
  }

  @org.jetbrains.annotations.NotNull
  private RuleDesignationKey getRuleDesignationKey(@RequestBody AlertRuleDTO alert, NodeDTO node, AlertKpiDTO alertKpi) {
    RuleDesignationKey key = new RuleDesignationKey();
    key.setRule(alertMapper.toEntity(alert));
    key.setNode(nodeMapper.dtoToEntity(node));
    key.setKpi(kpiMapper.toEntity(alertKpi.getKpiDTO()));
    return key;
  }

  private void checkForDeletableRuleDesignationsAndDelete(AlertRuleDTO alert) {
    List<RuleDesignationKey> oldKeys = new LinkedList<>();
    // add all existing keys of the alert rule to old keys
    for (RuleDesignation r : ruleDesignationService.getAll()) {
      if (r.getRule().getId() == alert.getId()) {
        oldKeys.add(r.getId());
      }
    }
    List<RuleDesignationKey> updatedKeys = new LinkedList<>();
    // for all nodes add a rule designation for each kpi
    if (alert.getNodes() != null) {
      for (NodeDTO n : alert.getNodes()) {
        for (AlertKpiDTO ak : alert.getKpis()) {
          RuleDesignationKey addKey = getRuleDesignationKey(alert, n, ak);
          if (!updatedKeys.contains(addKey)) {
            updatedKeys.add(addKey);
          }
        }
      }
    }
    // if old ruleset has keys that are not in the updated version delete those rule designations
    for (RuleDesignationKey oldKey : oldKeys) {
      if (!updatedKeys.contains(oldKey)) {
        ruleDesignationService.deleteRuleDesignation(oldKey);
      }
    }
  }

  /**
   * DELETE API for Alert Rules
   *
   * @param id Alert Rule Identifying id
   */
  @NotNull
  @DeleteMapping(value = "/alerts/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAlert(@NotNull @PathVariable("id") long id) {
    List<RuleDesignation> rds = ruleDesignationService.getAll();
    for (RuleDesignation rd : rds) {
      if (rd.getRule().getId() == id) {
        ruleDesignationService.deleteRuleDesignation(rd.getId());
      }
    }
    alertCrudService.deleteAlertRule(id);
  }
}

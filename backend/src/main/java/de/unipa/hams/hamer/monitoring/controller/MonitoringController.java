package de.unipa.hams.hamer.monitoring.controller;

import de.unipa.hams.hamer.domain.Record;
import de.unipa.hams.hamer.exception.AuthorizationContextException;
import de.unipa.hams.hamer.monitoring.controller.dto.RawRecordsDTO;
import de.unipa.hams.hamer.monitoring.service.api.MonitoringService;
import de.unipa.hams.hamer.persistence.api.KpiRepository;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import de.unipa.hams.hamer.security.service.api.AuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonitoringController {

  private final MonitoringService monitoringService;
  private final KpiRepository kpiRepository;
  private final AuthorizationService authorizationService;
  private final NodeRepository nodeRepository;

  @PostMapping("/monitoring/records")
  @PreAuthorize("hasRole('ROLE_NODE')")
  @ResponseStatus(HttpStatus.CREATED)
  public void addRecord(@RequestBody RawRecordsDTO dto) throws AuthorizationContextException {
    Date timestamp = new Date();
    for (RawRecordsDTO.KpiValueDTO v : dto.getKpis()) {
      Record record = new Record();
      record.setTimestamp(timestamp);
      record.setType(kpiRepository.findByName(v.getName()));
      var n = nodeRepository.getOne(authorizationService.currentNode());
      record.setSentBy(n);
      if (v.getValue() == null && (v.getName().equals("power_usage")
        || v.getName().equals("power_voltage")
        || v.getName().equals("power_current"))) { // only estimate power values
        record.setValue(monitoringService.estimatePower(v.getName(), timestamp));
      } else {
        record.setValue(v.getValue());
      }
      monitoringService.addRecord(record);
    }
  }

}

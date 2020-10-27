package de.unipa.hams.hamer.monitoring.schedule;

import de.unipa.hams.hamer.crud.service.api.NodeCrudService;
import de.unipa.hams.hamer.persistence.api.NodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


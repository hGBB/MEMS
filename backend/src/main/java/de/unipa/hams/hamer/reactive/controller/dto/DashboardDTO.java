package de.unipa.hams.hamer.reactive.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * Not much of a deal here. That's the representation of one certain
 * dashboard configuration at a time. It just holds a list of
 * {@link DashboardNodeDTO}s that refer to the nodes and their respctive
 * configurations.
 * <p>
 * Each time, dashboard settings change, this DTO shall be used to update
 * the backends perception of the dashboard.
 */
@Data
public class DashboardDTO {

  private List<DashboardNodeDTO> dashboardNodes;

}

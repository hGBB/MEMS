package de.unipa.hams.hamer.reactive.controller.dto;

import lombok.Data;

import java.util.Date;

/**
 * In each {@link DashboardDTO}, there is a number of node configurations,
 * expressed by this DTO. It can take on either live or history configuration,
 * depending on what should be delivered.
 * <p>
 * Fill both the flags (history *and* live), setting the desired one to
 * {@code true}, leaving the fields of the other mode {@code null}.
 */
@Data
public class DashboardNodeDTO {
  private long id;

  // Should be either history or live; leave others blank, except of the flag.

  private String mode;

  /*
   * History attributes
   */
  private Date historyStart;
  private Date historyEnd;

  /*
   * Live attributes
   */
  private Integer lastX;
  // private Short resolution;
}

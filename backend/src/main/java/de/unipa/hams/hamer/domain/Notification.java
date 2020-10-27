package de.unipa.hams.hamer.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@EqualsAndHashCode(of = {"id", "timestamp"})
@EntityListeners(AuditingEntityListener.class)
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private long id;

  @Column(nullable = false)
  @CreatedDate
  private Date timestamp;

  @Column(nullable = false)
  private String text;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private User recipient;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn
  private AlertRule triggeredBy;
}

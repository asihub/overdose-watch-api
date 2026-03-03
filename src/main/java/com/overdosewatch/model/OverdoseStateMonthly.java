package com.overdosewatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "overdose_state_monthly")
@Data
public class OverdoseStateMonthly {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "state", nullable = false)
  private String state;

  @Column(name = "state_name")
  private String stateName;

  @Column(name = "year", nullable = false)
  private Integer year;

  @Column(name = "month", nullable = false)
  private String month;

  @Column(name = "period")
  private String period;

  @Column(name = "indicator")
  private String indicator;

  @Column(name = "percent_complete")
  private BigDecimal percentComplete;

  @Column(name = "percent_pending_investigation")
  private BigDecimal percentPendingInvestigation;

  @Column(name = "footnote")
  private String footnote;

  @Column(name = "footnote_symbol")
  private String footnoteSymbol;

  @Column(name = "ingested_at")
  private LocalDateTime ingestedAt;
}
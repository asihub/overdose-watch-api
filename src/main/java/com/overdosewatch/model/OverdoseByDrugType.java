package com.overdosewatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "overdose_by_drug_type")
@Data
public class OverdoseByDrugType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "year", nullable = false)
  private Integer year;

  @Column(name = "indicator")
  private String indicator;

  @Column(name = "panel")
  private String panel;

  @Column(name = "unit")
  private String unit;

  @Column(name = "stub_name")
  private String stubName;

  @Column(name = "stub_label")
  private String stubLabel;

  @Column(name = "age")
  private String age;

  @Column(name = "estimate")
  private BigDecimal estimate;

  @Column(name = "ingested_at")
  private LocalDateTime ingestedAt;
}
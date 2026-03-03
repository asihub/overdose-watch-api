package com.overdosewatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "overdose_specific_drugs")
@Data
public class OverdoseSpecificDrug {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "data_as_of")
  private LocalDateTime dataAsOf;

  @Column(name = "death_year", nullable = false)
  private Integer deathYear;

  @Column(name = "death_month", nullable = false)
  private Integer deathMonth;

  @Column(name = "jurisdiction", nullable = false)
  private String jurisdiction;

  @Column(name = "drug_involved", nullable = false)
  private String drugInvolved;

  @Column(name = "time_period")
  private String timePeriod;

  @Column(name = "month_ending_date")
  private LocalDate monthEndingDate;

  @Column(name = "death_count")
  private Integer deathCount;

  @Column(name = "ingested_at")
  private LocalDateTime ingestedAt;
}
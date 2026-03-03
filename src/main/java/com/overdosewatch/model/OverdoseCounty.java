package com.overdosewatch.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "overdose_county")
@Data
public class OverdoseCounty {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "fips", nullable = false)
  private String fips;

  @Column(name = "fips_state")
  private String fipsState;

  @Column(name = "state")
  private String state;

  @Column(name = "county")
  private String county;

  @Column(name = "year", nullable = false)
  private Integer year;

  @Column(name = "population")
  private Integer population;

  @Column(name = "death_rate")
  private BigDecimal deathRate;

  @Column(name = "standard_deviation")
  private BigDecimal standardDeviation;

  @Column(name = "lower_95ci")
  private BigDecimal lower95ci;

  @Column(name = "upper_95ci")
  private BigDecimal upper95ci;

  @Column(name = "urban_rural")
  private String urbanRural;

  @Column(name = "census_division")
  private String censusDivision;

  @Column(name = "ingested_at")
  private LocalDateTime ingestedAt;
}
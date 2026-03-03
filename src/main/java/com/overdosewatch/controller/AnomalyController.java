package com.overdosewatch.controller;

import com.overdosewatch.repository.OverdoseSpecificDrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/anomalies")
@RequiredArgsConstructor
public class AnomalyController {

  private final OverdoseSpecificDrugRepository repository;

  /**
   * Detects jurisdictions where deaths increased by more than threshold%
   * comparing two year ranges.
   * Example: GET /api/v1/anomalies/detect?drug=Fentanyl&baselineYear=2020&currentYear=2023&threshold=20
   */
  @GetMapping("/detect")
  public ResponseEntity<List<Map<String, Object>>> detectAnomalies(
      @RequestParam(defaultValue = "Fentanyl") String drug,
      @RequestParam(defaultValue = "2020") Integer baselineYear,
      @RequestParam(defaultValue = "2023") Integer currentYear,
      @RequestParam(defaultValue = "20") Double threshold) {

    var baseline = repository.findDrugTrendsByJurisdiction("United States", baselineYear, baselineYear);

    // Get all jurisdictions for baseline year
    var baselineMap = new HashMap<String, Long>();
    var baselineByDrug = repository.findMonthlyTrend("United States", drug, baselineYear, baselineYear);

    // Get deaths by jurisdiction for baseline and current year
    var baselineData = getDeathsByJurisdiction(drug, baselineYear);
    var currentData = getDeathsByJurisdiction(drug, currentYear);

    var anomalies = new ArrayList<Map<String, Object>>();

    for (var entry : currentData.entrySet()) {
      String jurisdiction = entry.getKey();
      Long currentDeaths = entry.getValue();
      Long baselineDeaths = baselineData.getOrDefault(jurisdiction, 0L);

      if (baselineDeaths == 0) continue;

      double changePercent = ((double)(currentDeaths - baselineDeaths) / baselineDeaths) * 100;

      if (changePercent >= threshold) {
        Map<String, Object> anomaly = new LinkedHashMap<>();
        anomaly.put("jurisdiction", jurisdiction);
        anomaly.put("drug", drug);
        anomaly.put("baselineYear", baselineYear);
        anomaly.put("baselineDeaths", baselineDeaths);
        anomaly.put("currentYear", currentYear);
        anomaly.put("currentDeaths", currentDeaths);
        anomaly.put("changePercent", Math.round(changePercent * 10.0) / 10.0);
        anomalies.add(anomaly);
      }
    }

    anomalies.sort((a, b) -> Double.compare(
        (Double) b.get("changePercent"),
        (Double) a.get("changePercent")
    ));

    return ResponseEntity.ok(anomalies);
  }

  private Map<String, Long> getDeathsByJurisdiction(String drug, Integer year) {
    var results = repository.findDeathsByJurisdictionAndDrug(drug, year);
    var map = new HashMap<String, Long>();
    for (var row : results) {
      map.put((String) row[0], ((Number) row[1]).longValue());
    }
    return map;
  }
}
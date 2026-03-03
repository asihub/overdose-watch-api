package com.overdosewatch.controller;

import com.overdosewatch.repository.OverdoseSpecificDrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/trends")
@RequiredArgsConstructor
public class TrendController {

  private final OverdoseSpecificDrugRepository repository;

  /**
   * Returns total deaths by drug type for a jurisdiction and year range.
   * Example: GET /api/v1/trends/by-drug?jurisdiction=United States&fromYear=2020&toYear=2023
   */
  @GetMapping("/by-drug")
  public ResponseEntity<List<Map<String, Object>>> trendsByDrug(
      @RequestParam(defaultValue = "United States") String jurisdiction,
      @RequestParam(defaultValue = "2020") Integer fromYear,
      @RequestParam(defaultValue = "2023") Integer toYear) {

    var results = repository.findDrugTrendsByJurisdiction(jurisdiction, fromYear, toYear);

    var response = results.stream().map(row -> {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("drug", row[0]);
      map.put("totalDeaths", row[1]);
      return map;
    }).toList();

    return ResponseEntity.ok(response);
  }

  /**
   * Returns monthly trend for a specific drug and jurisdiction.
   * Example: GET /api/v1/trends/monthly?jurisdiction=United States&drug=Fentanyl&fromYear=2020&toYear=2023
   */
  @GetMapping("/monthly")
  public ResponseEntity<List<Map<String, Object>>> monthlyTrend(
      @RequestParam(defaultValue = "United States") String jurisdiction,
      @RequestParam(defaultValue = "Fentanyl") String drug,
      @RequestParam(defaultValue = "2020") Integer fromYear,
      @RequestParam(defaultValue = "2023") Integer toYear) {

    var results = repository.findMonthlyTrend(jurisdiction, drug, fromYear, toYear);

    var response = results.stream().map(row -> {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("year", row[0]);
      map.put("month", row[1]);
      map.put("totalDeaths", row[2]);
      return map;
    }).toList();

    return ResponseEntity.ok(response);
  }
}

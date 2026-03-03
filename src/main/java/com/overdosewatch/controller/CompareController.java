package com.overdosewatch.controller;

import com.overdosewatch.repository.OverdoseByDrugTypeRepository;
import com.overdosewatch.repository.OverdoseSpecificDrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/compare")
@RequiredArgsConstructor
public class CompareController {

  private final OverdoseByDrugTypeRepository drugTypeRepository;
  private final OverdoseSpecificDrugRepository specificDrugRepository;

  /**
   * Compares death rates by drug type over time.
   * Example: GET /api/v1/compare/rates?panel=All drug overdose deaths&fromYear=2015&toYear=2020
   */
  @GetMapping("/rates")
  public ResponseEntity<List<Map<String, Object>>> compareRates(
      @RequestParam(defaultValue = "All drug overdose deaths") String panel,
      @RequestParam(defaultValue = "2015") Integer fromYear,
      @RequestParam(defaultValue = "2020") Integer toYear) {

    var results = drugTypeRepository.findTotalRateByPanel(panel, fromYear, toYear);

    var response = results.stream().map(row -> {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("year", row[0]);
      map.put("panel", row[1]);
      map.put("ratePerHundredThousand", row[2]);
      return map;
    }).toList();

    return ResponseEntity.ok(response);
  }

  /**
   * Compares deaths across multiple jurisdictions for a specific drug.
   * Example: GET /api/v1/compare/jurisdictions?drug=Fentanyl&year=2023
   */
  @GetMapping("/jurisdictions")
  public ResponseEntity<List<Map<String, Object>>> compareJurisdictions(
      @RequestParam(defaultValue = "Fentanyl") String drug,
      @RequestParam(defaultValue = "2023") Integer year) {

    var results = specificDrugRepository.findDeathsByJurisdictionAndDrug(drug, year);

    var response = results.stream().map(row -> {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("jurisdiction", row[0]);
      map.put("drug", drug);
      map.put("year", year);
      map.put("totalDeaths", row[1]);
      return map;
    }).toList();

    return ResponseEntity.ok(response);
  }

  /**
   * Returns available drug panels for comparison.
   * Example: GET /api/v1/compare/panels
   */
  @GetMapping("/panels")
  public ResponseEntity<List<String>> getPanels() {
    return ResponseEntity.ok(drugTypeRepository.findDistinctPanels());
  }
}

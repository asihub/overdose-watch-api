package com.overdosewatch.repository;

import com.overdosewatch.model.OverdoseSpecificDrug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverdoseSpecificDrugRepository extends JpaRepository<OverdoseSpecificDrug, Integer> {

  @Query("""
        SELECT d.drugInvolved, SUM(d.deathCount) as totalDeaths
        FROM OverdoseSpecificDrug d
        WHERE d.jurisdiction = :jurisdiction
        AND d.deathYear BETWEEN :fromYear AND :toYear
        GROUP BY d.drugInvolved
        ORDER BY SUM(d.deathCount) DESC
    """)
  List<Object[]> findDrugTrendsByJurisdiction(
      @Param("jurisdiction") String jurisdiction,
      @Param("fromYear") Integer fromYear,
      @Param("toYear") Integer toYear
  );

  @Query("""
        SELECT d.deathYear, d.deathMonth, SUM(d.deathCount) as totalDeaths
        FROM OverdoseSpecificDrug d
        WHERE d.jurisdiction = :jurisdiction
        AND d.drugInvolved = :drug
        AND d.deathYear BETWEEN :fromYear AND :toYear
        GROUP BY d.deathYear, d.deathMonth
        ORDER BY d.deathYear, d.deathMonth
    """)
  List<Object[]> findMonthlyTrend(
      @Param("jurisdiction") String jurisdiction,
      @Param("drug") String drug,
      @Param("fromYear") Integer fromYear,
      @Param("toYear") Integer toYear
  );

  List<String> findDistinctDrugInvolvedBy();

  @Query("""
    SELECT d.jurisdiction, SUM(d.deathCount) as totalDeaths
    FROM OverdoseSpecificDrug d
    WHERE d.drugInvolved = :drug
    AND d.deathYear = :year
    GROUP BY d.jurisdiction
    ORDER BY SUM(d.deathCount) DESC
""")
  List<Object[]> findDeathsByJurisdictionAndDrug(
      @Param("drug") String drug,
      @Param("year") Integer year
  );
}

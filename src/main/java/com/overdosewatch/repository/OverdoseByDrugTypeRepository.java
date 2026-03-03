package com.overdosewatch.repository;

import com.overdosewatch.model.OverdoseByDrugType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverdoseByDrugTypeRepository extends JpaRepository<OverdoseByDrugType, Integer> {

  @Query("""
        SELECT d.year, d.panel, d.stubLabel, d.estimate
        FROM OverdoseByDrugType d
        WHERE d.year BETWEEN :fromYear AND :toYear
        AND d.stubName = 'Sex'
        AND d.age = 'All ages'
        ORDER BY d.year, d.panel
    """)
  List<Object[]> findRatesByYearRange(
      @Param("fromYear") Integer fromYear,
      @Param("toYear") Integer toYear
  );

  @Query("""
        SELECT DISTINCT d.panel
        FROM OverdoseByDrugType d
        ORDER BY d.panel
    """)
  List<String> findDistinctPanels();

  @Query("""
    SELECT d.year, d.panel, d.estimate
    FROM OverdoseByDrugType d
    WHERE d.panel = :panel
    AND d.stubLabel = 'All persons'
    AND d.age = 'All ages'
    AND d.year BETWEEN :fromYear AND :toYear
    ORDER BY d.year
""")
  List<Object[]> findTotalRateByPanel(
      @Param("panel") String panel,
      @Param("fromYear") Integer fromYear,
      @Param("toYear") Integer toYear
  );
}
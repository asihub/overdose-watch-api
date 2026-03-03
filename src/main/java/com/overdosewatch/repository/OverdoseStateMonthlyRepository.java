package com.overdosewatch.repository;

import com.overdosewatch.model.OverdoseStateMonthly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OverdoseStateMonthlyRepository extends JpaRepository<OverdoseStateMonthly, Integer> {

  @Query("""
        SELECT s.state, s.stateName, s.year, s.month,
               s.indicator, s.percentComplete
        FROM OverdoseStateMonthly s
        WHERE s.state = :state
        AND s.year = :year
        AND s.indicator = :indicator
        ORDER BY s.month
    """)
  List<Object[]> findByStateYearIndicator(
      @Param("state") String state,
      @Param("year") Integer year,
      @Param("indicator") String indicator
  );

  @Query("""
        SELECT DISTINCT s.indicator
        FROM OverdoseStateMonthly s
        ORDER BY s.indicator
    """)
  List<String> findDistinctIndicators();

  @Query("""
        SELECT DISTINCT s.state, s.stateName
        FROM OverdoseStateMonthly s
        ORDER BY s.state
    """)
  List<Object[]> findDistinctStates();
}
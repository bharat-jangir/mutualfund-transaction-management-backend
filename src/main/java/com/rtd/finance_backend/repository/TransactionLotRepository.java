package com.rtd.finance_backend.repository;

import com.rtd.finance_backend.model.TransactionLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;

@Repository
public interface TransactionLotRepository extends JpaRepository<TransactionLot, Integer> {
    
    // Find active lots for a specific PAN and scheme, ordered by purchase date (FIFO)
    @Query("SELECT tl FROM TransactionLot tl WHERE tl.pan = :pan AND tl.schemeId = :schemeId " +
           "AND tl.isActive = true AND tl.remainingUnits > 0 " +
           "ORDER BY tl.purchaseDate ASC")
    List<TransactionLot> findActiveLotsByPanAndSchemeOrderByPurchaseDate(
        @Param("pan") String pan, 
        @Param("schemeId") Integer schemeId
    );
    
    // Find all lots for a specific PAN and scheme
    List<TransactionLot> findByPanAndSchemeIdAndIsActiveTrueOrderByPurchaseDateAsc(
        String pan, Integer schemeId
    );
    
    // Find total remaining units for a PAN and scheme
    @Query("SELECT COALESCE(SUM(tl.remainingUnits), 0) FROM TransactionLot tl " +
           "WHERE tl.pan = :pan AND tl.schemeId = :schemeId AND tl.isActive = true")
    BigDecimal getTotalRemainingUnits(@Param("pan") String pan, @Param("schemeId") Integer schemeId);
    
    // Find total cost basis for remaining units
    @Query("SELECT COALESCE(SUM(tl.remainingUnits * tl.purchaseNav), 0) FROM TransactionLot tl " +
           "WHERE tl.pan = :pan AND tl.schemeId = :schemeId AND tl.isActive = true")
    BigDecimal getTotalCostBasis(@Param("pan") String pan, @Param("schemeId") Integer schemeId);
}

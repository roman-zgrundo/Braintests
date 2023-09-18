package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceHistoryRepo extends JpaRepository<BalanceHistory, Long> {
    @Query("SELECT SUM(bh.expense) FROM BalanceHistory bh WHERE bh.userId = :userId")
    BigDecimal getExpenseSumByUserId(@Param("userId") Long userId);

    @Query("SELECT bh.userId, SUM(bh.expense) FROM BalanceHistory bh GROUP BY bh.userId")
    List<Object[]> getExpenseSumByGroupUserId();

    @Query("SELECT COUNT(bh.id) FROM BalanceHistory bh WHERE bh.action = 'WRITE_OFF' AND bh.userId IN (SELECT u.id FROM User u WHERE u.teacher = :teacher)")
    Long getCountByTeacher(@Param("teacher") String teacherName);

    @Query("SELECT SUM(bh.expense) FROM BalanceHistory bh WHERE bh.userId = :userId")
    BigDecimal getBalanceByTeacher(@Param("userId") Long userId);


    List<BalanceHistory> findAllByUserId(Long userId);
}


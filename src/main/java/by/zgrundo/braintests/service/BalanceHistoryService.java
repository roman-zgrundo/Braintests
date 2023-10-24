package by.zgrundo.braintests.service;

import by.zgrundo.braintests.repository.BalanceHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class BalanceHistoryService {
    private final BalanceHistoryRepo balanceHistoryRepo;

    @Autowired
    public BalanceHistoryService(BalanceHistoryRepo balanceHistoryRepo) {
        this.balanceHistoryRepo = balanceHistoryRepo;
    }

    public LocalDateTime findLastExpenseDateByUserId(Long userId) {
        String lastExpenseDateStr = balanceHistoryRepo.findLastExpenseDateByUserId(userId);

        if (lastExpenseDateStr != null) {
            // Преобразовываем строку в LocalDate
            LocalDate localDate = LocalDate.parse(lastExpenseDateStr);

            // Создаем LocalDateTime с нулевым временем (00:00:00)
            return localDate.atStartOfDay();
        } else {
            return null;
        }
    }

}

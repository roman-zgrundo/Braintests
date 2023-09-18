package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.BalanceHistory;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.BalanceHistoryRepo;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class BalanceController {
    private final BalanceHistoryRepo balanceHistoryRepo;
    private final UserService userService;

    public BalanceController(UserService userService, BalanceHistoryRepo balanceHistoryRepo) {
        this.userService = userService;
        this.balanceHistoryRepo = balanceHistoryRepo;
    }


    @GetMapping("/accounts/profile/{id}/balance")
    public String balanceProfileForAdmin(@PathVariable(value = "id") long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        BigDecimal expenseSum = balanceHistoryRepo.getExpenseSumByUserId(id);
        List<BalanceHistory> balanceHistoryList = balanceHistoryRepo.findAllByUserId(id);
        // Сортировка списка balanceHistoryList по полю actionDate
        Collections.sort(balanceHistoryList, Comparator.comparing(BalanceHistory::getActionDate));

        String teacherName = userService.findOne(id).getTeacher();// Получаем имя учителя
        // Выполняем запрос на получение количества списаний у учеников
        Long count = balanceHistoryRepo.getCountByTeacher(userService.findOne(id).getName());
        // Получаем баланс учителя
        BigDecimal balance = balanceHistoryRepo.getBalanceByTeacher(userService.findOne(id).getId());
        // Проверяем, если balance равен null, устанавливаем его равным нулю
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        // Вычитаем количество списаний из баланса учителя
        balance = balance.subtract(new BigDecimal(count));

        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("balance_history", MainController.reverse(balanceHistoryList));
        model.addAttribute("expenseSum", expenseSum);
        model.addAttribute("teachear_balance", balance);

        return "balance";
    }


    @GetMapping("/profile/balance")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        BigDecimal expenseSum = balanceHistoryRepo.getExpenseSumByUserId(userDetails.getUser().getId());
        List<BalanceHistory> balanceHistoryList = balanceHistoryRepo.findAllByUserId(userDetails.getUser().getId());
        // Сортировка списка balanceHistoryList по полю actionDate
        Collections.sort(balanceHistoryList, Comparator.comparing(BalanceHistory::getActionDate));

        String teacherName = userService.findOne(userDetails.getUser().getId()).getTeacher();// Получаем имя учителя
        // Выполняем запрос на получение количества списаний у учеников
        Long count = balanceHistoryRepo.getCountByTeacher(userService.findOne(userDetails.getUser().getId()).getName());
        // Получаем баланс учителя
        BigDecimal balance = balanceHistoryRepo.getBalanceByTeacher(userService.findOne(userDetails.getUser().getId()).getId());
        // Проверяем, если balance равен null, устанавливаем его равным нулю
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        // Вычитаем количество списаний из баланса учителя
        balance = balance.subtract(new BigDecimal(count));

        model.addAttribute("teachear_balance", balance);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("balance_history", MainController.reverse(balanceHistoryList));
        model.addAttribute("expenseSum", expenseSum);

        return "balance";
    }

    @PostMapping("/accounts/profile/{user_id}/balance/{expense_id}/delete")
    public String expenseDelete(@PathVariable(value = "user_id") long user_id, @PathVariable(value = "expense_id") long expense_id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        balanceHistoryRepo.deleteById(expense_id);
        BigDecimal expenseSum = balanceHistoryRepo.getExpenseSumByUserId(user_id);
        model.addAttribute("user", userService.findOne(user_id));
        model.addAttribute("balance_history", MainController.reverse(balanceHistoryRepo.findAllByUserId(user_id)));
        model.addAttribute("expenseSum", expenseSum);

        return "redirect:/accounts/profile/{user_id}/balance";
    }

    @PostMapping("/accounts/profile/{userId}/balance/new_expence")
    public String PaymentAddInBalance(@RequestParam Long userId,
                                      @RequestParam BigDecimal expense,
                                      @RequestParam String comment,
                                      @RequestParam String actionDate,
                                      @RequestParam String action, Model model) {
        if (action.equals("WRITE_OFF")) {
            expense = expense.negate(); // Изменяем знак суммы на отрицательный для WRITE_OFF
        }
        BalanceHistory balanceHistory = new BalanceHistory(userId, expense, comment, actionDate, action);

        balanceHistoryRepo.save(balanceHistory);
        return "redirect:/accounts/profile/{userId}/balance";
    }


}

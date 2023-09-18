package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.BalanceHistory;
import by.zgrundo.braintests.models.Role;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.*;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StudentsController {

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final BalanceHistoryRepo balanceHistoryRepo;

    public StudentsController(BalanceHistoryRepo balanceHistoryRepo, UserService userService) {
        this.balanceHistoryRepo = balanceHistoryRepo;
        this.userService = userService;
    }


    @GetMapping("/students")
    public String Students(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Object[]> expenseSumList = balanceHistoryRepo.getExpenseSumByGroupUserId();
        Map<Long, BigDecimal> expenseSumMap = new HashMap<>();

        for (Object[] result : expenseSumList) {
            Long userId = (Long) result[0];
            BigDecimal expenseSum = (BigDecimal) result[1];
            expenseSumMap.put(userId, expenseSum);
        }

        Iterable<User> users = userRepository.findAll();
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("users", users);
        model.addAttribute("expenseSumMap", expenseSumMap);
        return "students";
    }

    @PostMapping("/students/changeStatus")
    public String ActiveUpdateStudentsPost(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long id, Model model) {
        userService.changeActive(id);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "redirect:/students";
    }

    @PostMapping("/students")
    public String PaymentAddInStudents(@RequestParam Long userId,
                                       @RequestParam BigDecimal expense,
                                       @RequestParam String comment,
                                       @RequestParam String actionDate,
                                       @RequestParam String action, Model model) {
        if (action.equals("WRITE_OFF")) {
            expense = expense.negate(); // Изменяем знак суммы на отрицательный для WRITE_OFF
        }
        BalanceHistory balanceHistory = new BalanceHistory(userId, expense, comment, actionDate, action);

        balanceHistoryRepo.save(balanceHistory);
        return "redirect:/students";
    }


    @GetMapping("/adminedituser/{id}")
    public String adminEditUser(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("usr", user);
        return "adminEditUser";
    }

    @PostMapping("/adminedituser/{id}")

    public String adminEditUserPost(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam String homework,
            @RequestParam String role,
            @RequestParam String teacher,
            Model model) {

        Role role2 = Role.valueOf(role);

        User user = userRepository.findById(id).orElseThrow();
        user.setName(name);
        user.setHomework(homework);
        user.setRole(role2);
        user.setTeacher(teacher);

        userRepository.save(user);
        return "redirect:/accounts";
    }


}

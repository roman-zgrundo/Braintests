package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.BalanceHistory;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.*;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class TeachersController {

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final BalanceHistoryRepo balanceHistoryRepo;

    public TeachersController(BalanceHistoryRepo balanceHistoryRepo, UserService userService) {
        this.balanceHistoryRepo = balanceHistoryRepo;
        this.userService = userService;
    }

    @PostMapping("/students/teacher/{id}")
    public String PaymentAddInStudentsForAdmin(@PathVariable(value = "id") long id_teacher,
                                               @RequestParam Long userId,
                                               @RequestParam BigDecimal expense,
                                               @RequestParam String comment,
                                               @RequestParam String actionDate,
                                               @RequestParam String action, Model model) {
        if (action.equals("WRITE_OFF")) {
            expense = expense.negate(); // Изменяем знак суммы на отрицательный для WRITE_OFF
        }

        // Получаем текущую дату и время
        LocalDateTime createdAt = LocalDateTime.now();

        BalanceHistory balanceHistory = new BalanceHistory(userId, expense, comment, actionDate, action, createdAt);

        balanceHistoryRepo.save(balanceHistory);
        return "redirect:/students/teacher/{id}";
    }

    @GetMapping("/teachers")
    public String Teachers(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Iterable<User> users = userRepository.findAll();
        List<Long> countList = new ArrayList<>();

        for (User user : users) {
            String teacherName = user.getTeacher(); // Получаем имя учителя

            // Выполняем запрос на получение количества списаний у учеников
            Long count = balanceHistoryRepo.getCountByTeacher(user.getName());

            // Получаем баланс учителя
            BigDecimal balance = balanceHistoryRepo.getBalanceByTeacher(user.getId());

            // Проверяем, если balance равен null, устанавливаем его равным нулю
            if (balance == null) {
                balance = BigDecimal.ZERO;
            }

            // Вычитаем количество списаний из баланса учителя
            balance = balance.subtract(new BigDecimal(count));

            // Добавляем результат в список countList
            countList.add(balance.longValue());
        }

        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("users", users);
        model.addAttribute("countList", countList); // Добавляем список count в модель

        return "teachers";
    }

    @PostMapping("/teachers")
    public String PaymentAddInTeachers(@RequestParam Long userId,
                                       @RequestParam BigDecimal expense,
                                       @RequestParam String comment,
                                       @RequestParam String actionDate,
                                       @RequestParam String action, Model model) {
        if (action.equals("WRITE_OFF")) {
            expense = expense.negate(); // Изменяем знак суммы на отрицательный для WRITE_OFF
        }

        // Получаем текущую дату и время
        LocalDateTime createdAt = LocalDateTime.now();

        BalanceHistory balanceHistory = new BalanceHistory(userId, expense, comment, actionDate, action, createdAt);

        balanceHistoryRepo.save(balanceHistory);
        return "redirect:/teachers";
    }



    @PostMapping("/teachers/changeStatus")
    public String ActiveUpdateTeachersPost(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long id, Model model) {
        userService.changeActive(id);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "redirect:/teachers";
    }

    @GetMapping("/students/teacher/{id}")
    public String StudentsForAdmin(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id_teacher, Model model) {
        List<Object[]> expenseSumList = balanceHistoryRepo.getExpenseSumByGroupUserId();
        Map<Long, BigDecimal> expenseSumMap = new HashMap<>();
        for (Object[] result : expenseSumList) {
            Long userId = (Long) result[0];
            BigDecimal expenseSum = (BigDecimal) result[1];
            expenseSumMap.put(userId, expenseSum);
        }
        Iterable<User> users = userRepository.findAll();
        List<User> activeUsers = new ArrayList<>();
        List<User> inactiveUsers = new ArrayList<>();

        for (User user : users) {
            if (user.isActive()) {
                activeUsers.add(user);
            } else {
                inactiveUsers.add(user);
            }
        }

        // Отсортируйте оба списка пользователей
        Collections.sort(activeUsers, Comparator.comparing(User::getName));
        Collections.sort(inactiveUsers, Comparator.comparing(User::getName));

        // Объедините списки
        List<User> sortedUsers = new ArrayList<>();
        sortedUsers.addAll(activeUsers);
        sortedUsers.addAll(inactiveUsers);

        model.addAttribute("user", userService.findOne(id_teacher));
        model.addAttribute("users", sortedUsers);
        model.addAttribute("id_teacher", id_teacher);
        model.addAttribute("expenseSumMap", expenseSumMap);
        return "studentsForAdmin";
    }

    @PostMapping("/students/teacher/{id_teacher}/changeStatus/{id}")
    public String ActiveUpdateStudentsForAdminPost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id_student, @PathVariable(value = "id_teacher") long id_teacher, @RequestParam Long id, Model model) {
        userService.changeActive(id);
        model.addAttribute("user", userService.findOne(id_student));
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "redirect:/students/teacher/{id_teacher}";
    }

}

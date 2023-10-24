package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.BalanceHistory;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.*;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AccountsController {

    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final Stat_RTRepo stat_rtRepo;
    private final Stat_ShtRepo stat_shtRepo;
    private final Stat_StroopRepo stat_stroopRepo;
    private final Stat_FlWRepo stat_flWRepo;
    private final BalanceHistoryRepo balanceHistoryRepo;

    public AccountsController(BalanceHistoryRepo balanceHistoryRepo, UserService userService, Stat_RTRepo stat_rtRepo, Stat_ShtRepo stat_shtRepo, Stat_StroopRepo stat_stroopRepo, Stat_FlWRepo stat_flWRepo) {
        this.balanceHistoryRepo = balanceHistoryRepo;
        this.userService = userService;
        this.stat_rtRepo = stat_rtRepo;
        this.stat_shtRepo = stat_shtRepo;
        this.stat_stroopRepo = stat_stroopRepo;
        this.stat_flWRepo = stat_flWRepo;
    }

    @PostMapping("/accounts")
    public String PaymentAddInAccounts(@RequestParam Long userId,
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
        return "redirect:/accounts";
    }

    @GetMapping("/accounts")
    public String Accounts(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
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

        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("users", sortedUsers);
        model.addAttribute("expenseSumMap", expenseSumMap);
        return "accounts";
    }

    @PostMapping("/accounts/changeStatus")
    public String ActiveUpdateAccountsPost(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long id, Model model) {
        userService.changeActive(id);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "redirect:/accounts";
    }

    @GetMapping("/accounts/profile/{id}")
    public String profile(@PathVariable(value = "id") long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
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

        model.addAttribute("teachear_balance", balance);
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("stat_rt", MainController.reverse(stat_rtRepo.findAllByUserId(id)));
        model.addAttribute("stat_sht", MainController.reverse(stat_shtRepo.findAllByUserId(id)));
        model.addAttribute("stat_stroop", MainController.reverse(stat_stroopRepo.findAllByUserId(id)));
        model.addAttribute("stat_flw", MainController.reverse(stat_flWRepo.findAllByUserId(id)) );
        model.addAttribute("expenseSum", expenseSum);
        return "profile";
    }

    @GetMapping("/accounts/profile/{id}/{count_sort}")
    public String profile(@PathVariable(value = "id") long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PathVariable int count_sort) {
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

        model.addAttribute("teachear_balance", balance);
        model.addAttribute("expenseSum", expenseSum);
        model.addAttribute("user", userService.findOne(id));
        model.addAttribute("stat_rt", MainController.reverse(stat_rtRepo.findAllByUserId(id)).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_sht", MainController.reverse(stat_shtRepo.findAllByUserId(id)).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_stroop", MainController.reverse(stat_stroopRepo.findAllByUserId(id)).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_flw", MainController.reverse(stat_flWRepo.findAllByUserId(id)).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        return "profile";
    }

    public static <T> List<T> reverse(List<T> list) {
        return IntStream.range(0, list.size())
                .map(i -> (list.size() - 1 - i))
                .mapToObj(list::get)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    @PostMapping("/accounts/deleteUser")
    @Transactional
    public String deleteUser(@RequestParam("userId") Long userId) {
        // Проверка наличия записей в таблице BalanceHistory для данного пользователя
        List<BalanceHistory> balanceHistoryEntries = balanceHistoryRepo.findAllByUserId(userId);

        // Перенаправление на страницу пользователей после удаления
        if (!balanceHistoryEntries.isEmpty()) {
            // Есть записи в BalanceHistory - вывести сообщение и не удалять
            // Здесь вы можете добавить логику отображения сообщения пользователю
            // о необходимости очистки баланса перед удалением.
            // Затем вернуть представление с сообщением, а не перенаправлять.
        } else {
            // Нет записей в BalanceHistory - удалить пользователя и его статистику
            stat_flWRepo.deleteByUserId(userId);
            stat_rtRepo.deleteByUserId(userId);
            stat_shtRepo.deleteByUserId(userId);
            stat_stroopRepo.deleteByUserId(userId);
            userRepository.deleteById(userId);

        }
        return "redirect:/accounts"; // Заглушка, может потребоваться изменить.
    }


}

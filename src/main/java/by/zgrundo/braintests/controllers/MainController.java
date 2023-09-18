package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.BalanceHistory;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.*;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import by.zgrundo.braintests.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/")
public class MainController {

    private final UserValidator userValidator;
    private final UserService userService;
    private final BalanceHistoryRepo balanceHistoryRepo;

    //    private final Running_textRepo running_textRepo;
    private final Stat_RTRepo stat_rtRepo;
    private final Stat_ShtRepo stat_shtRepo;
    private final Stat_StroopRepo stat_stroopRepo;
    private final Stat_FlWRepo stat_flWRepo;

    public MainController(BalanceHistoryRepo balanceHistoryRepo, UserValidator userValidator, UserService userService, Running_textRepo running_textRepo, Stat_RTRepo stat_rtRepo, Stat_ShtRepo stat_shtRepo, Stat_StroopRepo stat_stroopRepo, Stat_FlWRepo stat_flWRepo) {
        this.balanceHistoryRepo = balanceHistoryRepo;
        this.userValidator = userValidator;
        this.userService = userService;
        this.stat_rtRepo = stat_rtRepo;
        this.stat_shtRepo = stat_shtRepo;
        this.stat_stroopRepo = stat_stroopRepo;
        this.stat_flWRepo = stat_flWRepo;
    }


    @GetMapping("/")
    public String greeting(Model model) {
        return "home";
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registrationPage(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute("user") User user, Model model, Principal principal) {
        String namecreator = principal.getName();
//        Optional<User> creator = userService.findByName("user");
        model.addAttribute("creator", userService.findByName(namecreator));
//        model.addAttribute("creator", creator);
        return "registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.createUser(user);

        return "redirect:/";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        BigDecimal expenseSum = balanceHistoryRepo.getExpenseSumByUserId(userDetails.getUser().getId());
        Long id = userDetails.getUser().getId();

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
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("stat_rt", MainController.reverse(stat_rtRepo.findAllByUserId(userDetails.getUser().getId())) );
        model.addAttribute("stat_sht", MainController.reverse(stat_shtRepo.findAllByUserId(userDetails.getUser().getId())) );
        model.addAttribute("stat_stroop", MainController.reverse(stat_stroopRepo.findAllByUserId(userDetails.getUser().getId())) );
        model.addAttribute("stat_flw", MainController.reverse(stat_flWRepo.findAllByUserId(userDetails.getUser().getId())) );
        model.addAttribute("expenseSum", expenseSum);
        return "profile";
    }

    @GetMapping("/profile/{count_sort}")
    public String profile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, @PathVariable int count_sort) {
        BigDecimal expenseSum = balanceHistoryRepo.getExpenseSumByUserId(userDetails.getUser().getId());
        Long id = userDetails.getUser().getId();

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
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("stat_rt", MainController.reverse(stat_rtRepo.findAllByUserId(userDetails.getUser().getId())).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_sht", MainController.reverse(stat_shtRepo.findAllByUserId(userDetails.getUser().getId())).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_stroop", MainController.reverse(stat_stroopRepo.findAllByUserId(userDetails.getUser().getId())).stream()
                .limit(count_sort)
                .collect(Collectors.toList()));
        model.addAttribute("stat_flw", MainController.reverse(stat_flWRepo.findAllByUserId(userDetails.getUser().getId())).stream()
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


}



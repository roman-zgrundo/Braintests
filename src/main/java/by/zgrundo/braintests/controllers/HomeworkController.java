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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeworkController {

    @Autowired
    private UserRepository userRepository;

    public HomeworkController() {
    }

    @GetMapping("/homework/{id}")
    public String homework(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("usr", user);
        return "homework";
    }

    @PostMapping("/homework/{id}")

    public String homeworkPost(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam String homework,
            Model model) {

        User user = userRepository.findById(id).orElseThrow();
        user.setName(name);
        user.setHomework(homework);

        userRepository.save(user);
        return "redirect:/students";
    }


}

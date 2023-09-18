package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.dto.Data3;
import by.zgrundo.braintests.models.Stat_Stroop;
import by.zgrundo.braintests.repository.Stat_StroopRepo;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class StroopController {
    private final UserService userService;

    @Autowired
    private Stat_StroopRepo stat_stroopRepo;

    public StroopController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/stroop")
    public String stroop(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        return "stroop";
    }


    @PostMapping("/stroop")
    public String stroopStat(@RequestBody Data3 data3, Model model) {
        Stat_Stroop stat_stroop = new Stat_Stroop(data3.getIdUsr(), data3.getActionDate(), data3.getCorrect(), data3.getMistake(), data3.getTime());
        stat_stroopRepo.save(stat_stroop);
        return "redirect:/stroop";
    }
}

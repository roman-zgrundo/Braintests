package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.dto.Data2;
import by.zgrundo.braintests.models.Stat_Sht;
import by.zgrundo.braintests.repository.Stat_ShtRepo;
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
public class Schulte_TableController {


    private final UserService userService;
    @Autowired
    private Stat_ShtRepo stat_shtRepo;

    public Schulte_TableController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/schultetable")
    public String schultetable(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        return "schulte_table";
    }


    @PostMapping("/schultetable")
    public String schultetableStat(@RequestBody Data2 data2, Model model) {
        Stat_Sht stat_sht = new Stat_Sht(data2.getUser_id(), data2.getActionDate(), data2.getSize(), data2.getMistake(), data2.getTime());
        stat_shtRepo.save(stat_sht);
        return "redirect:/schultetable";
    }
}

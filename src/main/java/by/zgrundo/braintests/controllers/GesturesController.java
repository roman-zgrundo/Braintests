package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GesturesController {

    @GetMapping("/gestures")
    public String gestures(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        return "gestures";
    }
}

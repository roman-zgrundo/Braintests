package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.dto.Data;
import by.zgrundo.braintests.models.Running_text;
import by.zgrundo.braintests.models.Stat_RT;
import by.zgrundo.braintests.repository.Running_textRepo;
import by.zgrundo.braintests.repository.Stat_RTRepo;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class Running_textController {

    private final UserService userService;

    @Autowired
    public Running_textController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private Running_textRepo running_textRepo;

    @Autowired
    private Stat_RTRepo stat_rtRepo;

    @GetMapping("/runningtext")
    public String runningText(Model model) {
        Iterable<Running_text> texts = running_textRepo.findAll();
        List<Running_text> sortedTexts = StreamSupport.stream(texts.spliterator(), false)
                .sorted(Comparator.comparingInt(Running_text::getWorld_count))
                .collect(Collectors.toList());
        model.addAttribute("texts", sortedTexts);
        return "runningtext";
    }

    @GetMapping("/runningtext/{id}")
    public String runningTextStart(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        Optional<Running_text> running_text = running_textRepo.findById(id);
        ArrayList<Running_text> res = new ArrayList<>();
        running_text.ifPresent(res::add);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("text", res);
        return "runningTextStart";
    }

    @PostMapping("/runningtext")
    public String runningTextStartSTat(@RequestBody Data data, Model model) {
        Stat_RT stat_rt = new Stat_RT(data.getUser_id(), data.getActionDate(), data.getWorld_count(), data.getName_text(), data.getTime());
        stat_rtRepo.save(stat_rt);
        return "redirect:/runningtext";
    }

    @GetMapping("/runningtext/add")
    public String runningTextAdd(Model model) {
        return "runningtext-add";
    }

    @PostMapping("/runningtext/add")
    public String runningTextPostAdd(@RequestParam String name,
                                     @RequestParam String text,
                                     @RequestParam Integer world_count,
                                     @RequestParam String q1,
                                     @RequestParam String q2,
                                     @RequestParam String q3,
                                     @RequestParam String q4,
                                     @RequestParam String q5, Model model) {
        Running_text running_text = new Running_text(name, text, world_count, q1, q2, q3, q4, q5);

        running_textRepo.save(running_text);
        return "redirect:/runningtext";
    }

    @GetMapping("/editrt/{id}")
    public String runningTextEdit(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        Optional<Running_text> running_text = running_textRepo.findById(id);
        ArrayList<Running_text> res = new ArrayList<>();
        running_text.ifPresent(res::add);
        model.addAttribute("text", res);
        return "runningTextEdit";
    }

    @PostMapping("/editrt/{id}")
    public String runningTextPostUpdate(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam String text,
            @RequestParam Integer world_count,
            @RequestParam String q1,
            @RequestParam String q2,
            @RequestParam String q3,
            @RequestParam String q4,
            @RequestParam String q5, Model model) {

        Running_text running_text = running_textRepo.findById(id).orElseThrow();
        running_text.setName(name);
        running_text.setText(text);
        running_text.setWorld_count(world_count);
        running_text.setQ1(q1);
        running_text.setQ2(q2);
        running_text.setQ3(q3);
        running_text.setQ4(q4);
        running_text.setQ5(q5);

        running_textRepo.save(running_text);
        return "redirect:/runningtext";
    }


}

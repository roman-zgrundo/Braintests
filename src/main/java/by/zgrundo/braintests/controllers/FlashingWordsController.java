package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.dto.Data;
import by.zgrundo.braintests.dto.Data4;
import by.zgrundo.braintests.models.Flashing_words;
import by.zgrundo.braintests.models.Running_text;
import by.zgrundo.braintests.models.Stat_FlW;
import by.zgrundo.braintests.models.Stat_RT;
import by.zgrundo.braintests.repository.Flashing_wordsRepo;
import by.zgrundo.braintests.repository.Stat_FlWRepo;
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
public class FlashingWordsController {

    private final UserService userService;

    @Autowired
    public FlashingWordsController(UserService userService
            , Stat_FlWRepo stat_FlWRepo
    ) {
        this.userService = userService;
        this.stat_FlWRepo = stat_FlWRepo;
    }

    @Autowired
    private Flashing_wordsRepo flashing_wordsRepo;
    private final Stat_FlWRepo stat_FlWRepo;

    @GetMapping("/flashingwords")
    public String flashingWords(Model model) {
        Iterable<Flashing_words> words = flashing_wordsRepo.findAll();
        List<Flashing_words> sortedWords = StreamSupport.stream(words.spliterator(), false)
                .sorted(Comparator.comparingInt(Flashing_words::getWord_count))
                .collect(Collectors.toList());
        model.addAttribute("words", sortedWords);
        return "flashingWords";
    }


    @GetMapping("/flashingwords/{id}")
    public String flashingWordsStart(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        Optional<Flashing_words> words = flashing_wordsRepo.findById(id);
        ArrayList<Flashing_words> res = new ArrayList<>();
        words.ifPresent(res::add);
        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
        model.addAttribute("words", res);
        return "flashingWordsStart";
    }

    @PostMapping("/flashingwords")
    public String runningTextStartSTat(@RequestBody Data4 data4, Model model) {
        Stat_FlW stat_flW = new Stat_FlW(data4.getUser_id(), data4.getActionDate(), data4.getWord_count(), data4.getName_set(), data4.getMistake(), data4.getSpeed());
        stat_FlWRepo.save(stat_flW);
        return "redirect:/flashingwords";
    }

    @GetMapping("/editflashingwords/{id}")
    public String flashingWordsEdit(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        Optional<Flashing_words> flashing_words = flashing_wordsRepo.findById(id);
        ArrayList<Flashing_words> res = new ArrayList<>();
        flashing_words.ifPresent(res::add);
        model.addAttribute("words", res);
        return "flashingWordsEdit";
    }

    @PostMapping("/editflashingwords/{id}")
    public String flashingWordsPostUpdate(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam String words,
            @RequestParam Integer word_count,
            Model model) {

        Flashing_words flashing_words = flashing_wordsRepo.findById(id).orElseThrow();
        flashing_words.setName(name);
        flashing_words.setWords(words);
        flashing_words.setWord_count(word_count);

        flashing_wordsRepo.save(flashing_words);
        return "redirect:/flashingwords";
    }

    @GetMapping("/flashingwords/add")
    public String flashingWordsAdd(Model model) {
        return "flashingWords-add";
    }

    @PostMapping("/flashingwords/add")
    public String flashingWordsPostAdd(@RequestParam String name,
                                       @RequestParam String words,
                                       @RequestParam Integer word_count,
                                       Model model) {
        Flashing_words flashing_words = new Flashing_words(name, words, word_count);

        flashing_wordsRepo.save(flashing_words);
        return "redirect:/flashingwords";
    }

}

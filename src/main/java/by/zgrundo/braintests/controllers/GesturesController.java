package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.Flashing_words;
import by.zgrundo.braintests.models.WordsForGestures;
import by.zgrundo.braintests.repository.Gestures_wordsRepo;
import by.zgrundo.braintests.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Controller
public class GesturesController {

    @Autowired
    private Gestures_wordsRepo gestures_wordsRepo;

    @GetMapping("/gestures")
    public String gestures(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
//        model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));

        Optional<WordsForGestures> words = StreamSupport.stream(gestures_wordsRepo.findAll().spliterator(), false).findFirst();
        ArrayList<WordsForGestures> res = new ArrayList<>();
        words.ifPresent(res::add);
        model.addAttribute("words", res);

        return "gestures";
    }


    @GetMapping("/gestureswords/add")
    public String gesturesWordsAdd(Model model) {
        return "gesturesWords-add";
    }

    @PostMapping("/gestureswords/add")
    public String gesturesWordsPostAdd(@RequestParam String words, Model model) {
        WordsForGestures start_gestures_words = new WordsForGestures("start words");

        gestures_wordsRepo.save(start_gestures_words);
        return "redirect:/";
    }


    @GetMapping("/editgestureswords")
    public String gesturesWordsEdit(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (gestures_wordsRepo.count() == 0) {
            WordsForGestures start_gestures_words = new WordsForGestures("Яблоко Банан Груша");
            gestures_wordsRepo.save(start_gestures_words);
        }

        Optional<WordsForGestures> singleGesturesWords = StreamSupport.stream(gestures_wordsRepo.findAll().spliterator(), false).findFirst();

        ArrayList<WordsForGestures> res = new ArrayList<>();
        singleGesturesWords.ifPresent(res::add);
        model.addAttribute("words", res);
        return "gesturesWordsEdit";
    }

    @PostMapping("/editgestureswords")
    public String gesturesWordsPostUpdate(
            @RequestParam String words,
            Model model) {

        long id = 1; // Значение по умолчанию, если запись не найдена
        Optional<WordsForGestures> singleGesturesWords = StreamSupport.stream(gestures_wordsRepo.findAll().spliterator(), false).findFirst();
        if (singleGesturesWords.isPresent()) {
            id = singleGesturesWords.get().getId();
        }

        WordsForGestures gestures_words = gestures_wordsRepo.findById(id).orElseThrow();

        gestures_words.setWords(words);

        gestures_wordsRepo.save(gestures_words);
        return "redirect:/gestures";
    }

}
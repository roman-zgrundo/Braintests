package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.dto.Data;
import by.zgrundo.braintests.models.Running_text;
import by.zgrundo.braintests.models.SpeedAlphabetLetters;
import by.zgrundo.braintests.models.SpeedAlphabetTexts;
import by.zgrundo.braintests.models.Stat_RT;
import by.zgrundo.braintests.repository.Running_textRepo;
import by.zgrundo.braintests.repository.SpeedAlphabetLettersRepo;
import by.zgrundo.braintests.repository.SpeedAlphabetTextsRepo;
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
@RequestMapping("/editspeedalphabet")
public class SpeedAlphabetController {

    @Autowired
    private SpeedAlphabetLettersRepo lettersRepo;

    @Autowired
    private SpeedAlphabetTextsRepo textsRepo;

    // Главная страница для редактирования
    @GetMapping
    public String editSpeedAlphabet(Model model) {
        List<SpeedAlphabetLetters> letters = lettersRepo.findAll();
        model.addAttribute("letters", letters);
        return "editSpeedAlphabet"; // Имя шаблона Thymeleaf
    }

    // Добавление новой буквы
    @PostMapping("/addletter")
    public String addLetter(@RequestParam String letter, @RequestParam int seq) {
        SpeedAlphabetLetters newLetter = new SpeedAlphabetLetters(letter, seq);
        lettersRepo.save(newLetter);
        return "redirect:/editspeedalphabet";
    }

    // Удаление буквы
    @PostMapping("/deleteletter/{id}")
    public String deleteLetter(@PathVariable Long id) {
        Optional<SpeedAlphabetLetters> letter = lettersRepo.findById(id);
        if (letter.isPresent()) {
            // Удаляем связанные тексты перед удалением буквы
            List<SpeedAlphabetTexts> texts = textsRepo.findByLetter(letter.get());
            textsRepo.deleteAll(texts);
            lettersRepo.delete(letter.get());
        }
        return "redirect:/editspeedalphabet";
    }

    // Загрузка текстов для выбранной буквы
    @GetMapping("/texts/{letterId}")
    @ResponseBody
    public List<SpeedAlphabetTexts> getTextsByLetter(@PathVariable Long letterId) {
        Optional<SpeedAlphabetLetters> letter = lettersRepo.findById(letterId);
        return letter.map(textsRepo::findByLetter).orElse(List.of());
    }
}
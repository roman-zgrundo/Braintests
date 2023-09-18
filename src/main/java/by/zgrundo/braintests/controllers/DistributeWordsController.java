package by.zgrundo.braintests.controllers;

import by.zgrundo.braintests.models.Distribute_words;
import by.zgrundo.braintests.models.Flashing_words;
import by.zgrundo.braintests.repository.Distribute_wordsRepo;
import by.zgrundo.braintests.security.CustomUserDetails;
import by.zgrundo.braintests.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class DistributeWordsController {
    private final UserService userService;
    private final Distribute_wordsRepo distribute_wordsRepo;

    public DistributeWordsController(UserService userService, Distribute_wordsRepo distribute_wordsRepo) {
        this.userService = userService;
        this.distribute_wordsRepo = distribute_wordsRepo;
    }

    @GetMapping("/distributewords")
    public String distributeWords(Model model) {
        Iterable<Distribute_words> topics = distribute_wordsRepo.findAll();
        model.addAttribute("topics", topics);
        return "distributeWords";
    }

    @GetMapping("/distributewords/{id}")
    public String distributeWordsStart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable(value = "id") long id, Model model) {
        Optional<Distribute_words> topic = distribute_wordsRepo.findById(id);

        if (topic.isPresent()) {
            Distribute_words distributeWords = topic.get();
            List<String> column1Words = Arrays.asList(distributeWords.getColumn1().split("\\s+"));
            List<String> column2Words = Arrays.asList(distributeWords.getColumn2().split("\\s+"));

            model.addAttribute("id", id); // передача id в модель
            model.addAttribute("user", userService.findOne(userDetails.getUser().getId()));
            model.addAttribute("topic", distributeWords);
            model.addAttribute("column1Words", column1Words);
            model.addAttribute("column2Words", column2Words);

            return "distributeWordsStart";
        } else {
            // Handle case when the topic is not found
            return "error"; // You can choose an appropriate error page
        }
    }

    @GetMapping("/distributewords/add")
    public String distributewordsAdd(Model model) {
        return "distributewords-add";
    }

    @PostMapping("/distributewords/add")
    public String flashingWordsPostAdd(@RequestParam String topic,
                                       @RequestParam String name1,
                                       @RequestParam String name2,
                                       @RequestParam String column1,
                                       @RequestParam String column2,
                                       Model model) {
        Distribute_words distribute_words = new Distribute_words(topic, name1, name2, column1, column2);

        distribute_wordsRepo.save(distribute_words);
        return "redirect:/distributewords";
    }


    @GetMapping("/editdistributewords/{id}")
    public String DistributeWordsEdit(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(value = "id") long id, Model model) {
        Optional<Distribute_words> distribute_words = distribute_wordsRepo.findById(id);
        ArrayList<Distribute_words> topic = new ArrayList<>();
        distribute_words.ifPresent(topic::add);
        model.addAttribute("topic", topic);
        return "distributeWordsEdit";
    }

    @PostMapping("/editdistributewords/{id}")
    public String DistributeWordsPostUpdate(
            @PathVariable(value = "id") long id,
            @RequestParam String topic,
            @RequestParam String name1,
            @RequestParam String name2,
            @RequestParam String column1,
            @RequestParam String column2,
            Model model) {

        Distribute_words distribute_words = distribute_wordsRepo.findById(id).orElseThrow();
        distribute_words.setTopic(topic);
        distribute_words.setName1(name1);
        distribute_words.setName2(name2);
        distribute_words.setColumn1(column1);
        distribute_words.setColumn2(column2);

        distribute_wordsRepo.save(distribute_words);
        return "redirect:/distributewords";
    }

}

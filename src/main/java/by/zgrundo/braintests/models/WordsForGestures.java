package by.zgrundo.braintests.models;

import by.zgrundo.braintests.repository.Gestures_wordsRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.*;

@Getter
@Setter
@Entity
public class WordsForGestures {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String words;

    public WordsForGestures(String words) {
        this.words = words;
    }

    public WordsForGestures() {
    }

}
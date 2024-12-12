package by.zgrundo.braintests.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Flashing_words {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    private String words;
    private int word_count;

    public Flashing_words(String name, String words, int word_count) {
        this.name = name;
        this.words = words;
        this.word_count = word_count;
    }

    public Flashing_words() {
    }
}

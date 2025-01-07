package by.zgrundo.braintests.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SpeedAlphabetLetters {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String letter;

    @Column(nullable = false)
    private Integer seq;

    public SpeedAlphabetLetters(String letter, Integer seq) {
        this.letter = letter;
        this.seq = seq;
    }

    public SpeedAlphabetLetters() {
    }

}

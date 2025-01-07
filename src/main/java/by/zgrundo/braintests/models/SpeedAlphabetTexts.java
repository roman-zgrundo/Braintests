package by.zgrundo.braintests.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class SpeedAlphabetTexts {   // азбука скорости

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "letter_id", nullable = false)
    private SpeedAlphabetLetters letter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpeedAlphabetTextType type;

    @Column(nullable = false)
    private Integer seq;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    public SpeedAlphabetTexts(SpeedAlphabetLetters letter, SpeedAlphabetTextType type, Integer seq, String content) {
        this.letter = letter;
        this.type = type;
        this.seq = seq;
        this.content = content;
    }

    public SpeedAlphabetTexts() {
    }
}
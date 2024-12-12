package by.zgrundo.braintests.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Running_text {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String text;
    private String q1, q2, q3, q4, q5;
    private int world_count;

    public Running_text(String name, String text, int world_count, String q1,
                        String q2, String q3, String q4, String q5) {
        this.name = name;
        this.text = text;
        this.world_count = world_count;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
    }

    public Running_text() {
    }
}

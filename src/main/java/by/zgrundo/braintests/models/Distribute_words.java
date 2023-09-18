package by.zgrundo.braintests.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Distribute_words {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String topic, name1, name2;

    @Column(columnDefinition = "LONGTEXT")
    private String column1;

    @Column(columnDefinition = "LONGTEXT")
    private String column2;


    public Distribute_words(String topic, String name1, String name2, String column1,  String column2) {
        this.topic = topic;
        this.name1 = name1;
        this.name2 = name2;
        this.column1 = column1;
        this.column2 = column2;
    }

    public Distribute_words() {
    }
}

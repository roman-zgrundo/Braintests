package by.zgrundo.braintests.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity

public class Stat_FlW {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String actionDate;
    private Long userId;

    private Integer wordCount;
    private String nameSet;
    private Integer mistake;
    private Float speed;

    public Stat_FlW() {
    }

    public Stat_FlW(Long userId, String actionDate, Integer wordCount, String nameSet, Integer mistake, Float speed) {
        this.userId = userId;
        this.actionDate = actionDate;
        this.wordCount = wordCount;
        this.nameSet = nameSet;
        this.mistake = mistake;
        this.speed = speed;
    }


}

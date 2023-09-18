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

public class Stat_RT {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String actionDate;
    private Long userId;

    private Integer worldCount;
    private String nameText;
    private Float time;

    public Stat_RT() {
    }

    public Stat_RT(Long userId, String actionDate, Integer worldCount, String nameText, Float time) {
        this.userId = userId;
        this.actionDate = actionDate;
        this.worldCount = worldCount;
        this.nameText = nameText;
        this.time = time;
    }


}
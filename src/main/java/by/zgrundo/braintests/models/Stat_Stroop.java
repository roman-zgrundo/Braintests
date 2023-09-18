package by.zgrundo.braintests.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity

public class Stat_Stroop {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    private String actionDate;

    private Integer correct;
    private Integer mistake;
    private Float time;

    public Stat_Stroop() {
    }

    public Stat_Stroop(Long userId, String actionDate, Integer correct, Integer mistake, Float time) {
        this.userId = userId;
        this.actionDate = actionDate;
        this.correct = correct;
        this.mistake = mistake;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Stat_Stroop{" +
                "id=" + id +
                ", userId=" + userId +
                ", actionDate=" + actionDate +
                ", correct=" + correct +
                ", mistake=" + mistake +
                ", time=" + time +
                '}';
    }
}

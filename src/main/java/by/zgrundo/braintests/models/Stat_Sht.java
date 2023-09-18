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

public class Stat_Sht {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String actionDate;
    private Long userId;

    private Integer size;
    private Integer mistake;
    private Float time;

    public Stat_Sht() {
    }

    public Stat_Sht(Long userId, String actionDate, Integer size, Integer mistake, Float time) {
        this.userId = userId;
        this.actionDate = actionDate;
        this.size = size;
        this.mistake = mistake;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Stat_Sht{" +
                "id=" + id +
                ", userId=" + userId +
                ", size=" + size +
                ", mistake=" + mistake +
                ", time=" + time +
                '}';
    }
}

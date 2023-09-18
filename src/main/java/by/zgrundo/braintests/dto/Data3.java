package by.zgrundo.braintests.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Data3 {
    Long idUsr;
    String actionDate;
    Integer correct;
    Integer mistake;
    Float time;
}

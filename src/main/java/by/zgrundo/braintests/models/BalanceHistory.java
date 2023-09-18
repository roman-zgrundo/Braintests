package by.zgrundo.braintests.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor

@Table(name = "balance_history")
public class BalanceHistory {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "expense")
    private BigDecimal expense;

    @Column(name = "comment")
    private String comment;

    @Column(name = "action_date")
    private String actionDate;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private ActionWithBalance action;

    public BalanceHistory(Long userId, BigDecimal expense, String comment, String actionDate,
                        String action) {
        this.userId = userId;
        this.expense = expense;
        this.comment = comment;
        this.actionDate = actionDate;
        this.action = ActionWithBalance.valueOf(action);
    }
}
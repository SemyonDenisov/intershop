package ru.yandex.payment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name="balance")
@Getter
@NoArgsConstructor
public class Balance {
    @Id
    Integer id;
    @Setter
    Double amount;

    Integer userId;

    public Balance(Double balance, Integer userId) {
        this.amount = balance;
        this.userId = userId;
    }
}

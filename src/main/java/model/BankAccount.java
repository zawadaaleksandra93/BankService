package model;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    @NonNull
    private String pesel;
    @NonNull
    private BigDecimal value;
    @NonNull
    private String accountNumber;

    public void addMoney(BigDecimal transfer){
        value = this.value.add(transfer);
    };
    public void deductMoney(BigDecimal transfer){
        value = this.value.add(transfer.negate());
    };
}

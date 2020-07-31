package request;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.security.PublicKey;

@Builder
@Value
public class CreateBankAccountRequest {
    @NonNull
    private String pesel;
    @NonNull
    private BigDecimal initialValue;

    public CreateBankAccountRequest(@NonNull String pesel, @NonNull BigDecimal initialValue) {
        this.pesel = pesel;
        this.initialValue = initialValue;
    }
}

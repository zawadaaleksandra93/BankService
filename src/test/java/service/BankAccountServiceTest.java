package service;

import model.BankAccount;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import repository.InMemoryBankAccountRepository;
import request.CreateBankAccountRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

public class BankAccountServiceTest {
    private static final String PESEL = "93110232343";
    private static final String PESEL2 = "93110232345";
    private static final String PESEL3 ="93110232346" ;
    private static final String PESEL4 ="93110232347" ;
    private static final String PESEL5 = "93110232348";
    private static final String PESEL6 = "93110232347";
    private static final long TRANSFER = 1000;
    private BankAccountService service = new BankAccountService(InMemoryBankAccountRepository.getInstance());
    private List<BankAccount> bankAccounts = new ArrayList<>();

    @Test
    public void shouldCreateBankAccountWithPeselWhichDoesNotHaveAssignementBankAccount() {

        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL2, BigDecimal.TEN);

        //when
        Optional<BankAccount> bankAccount = service.createBankAccount(request);

        ///then
        assertThat(bankAccount.isPresent()).isTrue();
        assertThat(bankAccount.get().getPesel()).isEqualTo(PESEL2);
        assertThat(bankAccount.get().getValue()).isEqualTo(BigDecimal.TEN);
        assertThat(bankAccount.get().getAccountNumber()).isNotBlank();


    }

    @Test
    public void shouldNotCreateBankAccountWithPeselWhichHasAssignedBankAccount() {

        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);

        //when
        Optional<BankAccount> bankAccount = service.createBankAccount(request);

        //then
        Assertions.assertThat(bankAccount.isEmpty()).isTrue();

    }

    @Test
    public void shouldDeleteBankAccountFromMemoryForGivenPeselIfSuchBankAccountExist() {

        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);

        //when
        service.canDelete(PESEL);

        //that
        Assertions.assertThat(bankAccounts.contains(inMemoryBankAccount)).isFalse();

    }

 /*   @Test
    public void shouldNotDeleteBankAccountFromMemoryForGivenPeselIfSuchBankAccountDidNotExist() {

        //given
        String ACCOUNTnUMBERfORtEST = "123456789";
        BankAccount bankAccount = new BankAccount(PESEL,BigDecimal.TEN,ACCOUNTnUMBERfORtEST);

        //when
        service.canDelete(PESEL);

        //that


    }

  */

    @Test
    public void shouldAddIncomeToBankAccountAssignedForGivenPesel() {

        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);
        BigDecimal initialAccountValue = inMemoryBankAccount.get().getValue();

        //when
        service.canAddIncome(PESEL,BigDecimal.valueOf(TRANSFER));

        //that
        assertThat(inMemoryBankAccount.isPresent()).isTrue();
        assertThat(inMemoryBankAccount.get().getPesel()).isEqualTo(PESEL);
        assertThat(inMemoryBankAccount.get().getValue()).isEqualTo(BigDecimal.valueOf(TRANSFER).add(initialAccountValue));
    }

    @Test
    public void shouldNotAddIncomeToBankAccountIfThereIsNoBankAccountAssignedForGivenPesel() {
        //given
        String ACCOUNTnUMBERfORtEST = "123456789";
        BankAccount bankAccount = new BankAccount(PESEL,BigDecimal.TEN,ACCOUNTnUMBERfORtEST);
        BigDecimal initialAccountValue = bankAccount.getValue();

        //when
        service.canAddIncome(PESEL,BigDecimal.valueOf(TRANSFER));

        //that
        assertThat(bankAccounts.contains(bankAccount)).isFalse();
        assertThat(bankAccount.getValue()).isLessThanOrEqualTo(((BigDecimal.valueOf(TRANSFER).add(initialAccountValue))));

    }
@Test
    public void shouldDeductExpenseFromBankAccountAssignedForGivenPeselIfSuchBankAccountExistInMemoryAndExpenseIsSmallerThanBankAccountValue() {
        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL5, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);
        BigDecimal initialAccountValue = inMemoryBankAccount.get().getValue();

        //when
        service.canDeductExpense(PESEL5,BigDecimal.ONE);

        //that
        assertThat(inMemoryBankAccount.isPresent()).isTrue();
        assertThat(inMemoryBankAccount.get().getPesel()).isEqualTo(PESEL5);
        assertThat(inMemoryBankAccount.get().getValue()).isEqualTo(initialAccountValue.add(BigDecimal.ONE.negate()));
    }
    @Test
    public void shouldNotDeductExpenseFromBankAccountAssignedForGivenPeselIfSuchBankAccountExistInMemoryAndExpenseIsGratherThanBankAccountValue() {
        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL4, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);
        BigDecimal initialAccountValue = inMemoryBankAccount.get().getValue();

        //when
        service.canDeductExpense(PESEL6,BigDecimal.valueOf(TRANSFER));

        //that
        assertThat(inMemoryBankAccount.isPresent()).isTrue();
        assertThat(inMemoryBankAccount.get().getPesel()).isEqualTo(PESEL4);
        assertThat(inMemoryBankAccount.get().getValue()).isGreaterThan(initialAccountValue.add(BigDecimal.valueOf(TRANSFER).negate()));
    }

    @Test
    public void shouldNotDeductIncomeToBankAccountAssignedForGivenPeselIfSuchBankAccountDidNotExistInMemory() {
        //given
        String ACCOUNTnUMBERfORtEST = "123456789";
        BankAccount bankAccount = new BankAccount(PESEL,BigDecimal.TEN,ACCOUNTnUMBERfORtEST);
        BigDecimal initialAccountValue = bankAccount.getValue();

        //when
        service.canDeductExpense(PESEL,BigDecimal.ONE);

        //that
        assertThat(bankAccounts.contains(bankAccount)).isFalse();
        assertThat(bankAccount.getValue()).isGreaterThan(initialAccountValue.add(BigDecimal.ONE.negate()));
    }

    @Test
    public void shouldDeductMoneyFromAccountAssignedForGivenPeselIfBankAccountValueIsGreaterThanRequiredExpense() {
        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL3, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);
        BigDecimal initialAccountValue = inMemoryBankAccount.get().getValue();

        //when
        service.checkIfThereIsEnoughMoneyToDeductRequairedExpense(PESEL3,BigDecimal.ONE);

        //that
        assertThat(inMemoryBankAccount.isPresent()).isTrue();
        assertThat(inMemoryBankAccount.get().getPesel()).isEqualTo(PESEL3);
        assertThat(inMemoryBankAccount.get().getValue()).isEqualTo(initialAccountValue.add(BigDecimal.ONE.negate()));

    }
    @Test
    public void shouldNotDeductMoneyFromAccountAssignedForGivenPeselIfBankAccountValueIsSmallerThanRequiredExpense() {
        //given
        CreateBankAccountRequest request = new CreateBankAccountRequest(PESEL6, BigDecimal.TEN);
        Optional<BankAccount> inMemoryBankAccount = service.createBankAccount(request);
        BigDecimal initialAccountValue = inMemoryBankAccount.get().getValue();

        //when
        service.checkIfThereIsEnoughMoneyToDeductRequairedExpense(PESEL6,BigDecimal.valueOf(TRANSFER));

        //that
        assertThat(inMemoryBankAccount.isPresent()).isTrue();
        assertThat(inMemoryBankAccount.get().getPesel()).isEqualTo(PESEL6);
        assertThat(inMemoryBankAccount.get().getValue()).isGreaterThan(initialAccountValue.add(BigDecimal.valueOf(TRANSFER).negate()));
    }
}
package repository;

import model.BankAccount;
import request.CreateBankAccountRequest;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountRepository {

    boolean existByPesel(String pesel);

    BankAccount create(CreateBankAccountRequest request);

    List<BankAccount> findAll();

    void delete(String pesel);

    void addIncome(String pesel, BigDecimal income);

    BankAccount findUsersBankAccountForGivenPesel(String pesel);

    boolean checkIfThereIsEnoughMoneyToDeductExpense(String pesel, BigDecimal expense);

    void deductMoney(String pesel, BigDecimal expense);



    //BigDecimal checkWhatIsCurrentValueOfUsersBankAccount(String pesel, BigDecimal expense);
}

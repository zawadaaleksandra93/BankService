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



    void deductExpenses(String pesel, BigDecimal expense);
}

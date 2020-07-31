package repository;

import model.BankAccount;
import request.CreateBankAccountRequest;

import java.util.List;

public interface BankAccountRepository {

    boolean existByPesel(String pesel);

    BankAccount create(CreateBankAccountRequest request);

    List<BankAccount> findAll();
}

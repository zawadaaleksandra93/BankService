package service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.BankAccount;
import repository.BankAccountRepository;
import repository.InMemoryBankAccountRepository;
import request.CreateBankAccountRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@AllArgsConstructor
@NoArgsConstructor
public class BankAccountService {

    private BankAccountRepository repository;


    public Optional<BankAccount> createBankAccount(CreateBankAccountRequest request) {
        boolean existByPesel = repository.existByPesel(request.getPesel());
        if (existByPesel) {
            return Optional.empty();
        }
        return Optional.of(repository.create(request));
    }

    public List<BankAccount> findAll() {
        return repository.findAll();
    }

    public boolean canDelete(String pesel) {
        if (repository.existByPesel(pesel)) {
            repository.delete(pesel);
            return true;
        }
        return false;
    }

    public boolean canAddIncome(String pesel, BigDecimal income) {
        if (repository.existByPesel(pesel)) {
            repository.addIncome(pesel, income);
            return true;
        }
        return false;
    }

    public boolean canDeductExpense(String pesel, BigDecimal expense) {
        if (repository.existByPesel(pesel)){
            checkIfThereIsEnoughMoneyToDeductRequairedExpense(pesel,expense);
            return true;
        }
        return false;
    }
    public boolean checkIfThereIsEnoughMoneyToDeductRequairedExpense(String pesel, BigDecimal expense){
        if (repository.checkIfThereIsEnoughMoneyToDeductExpense(pesel,expense)){
            repository.deductMoney(pesel, expense);
            System.out.println("done");
            return true;
        }
        System.out.println("there is not enough money to make this transfer.");
        return false;
    }
}

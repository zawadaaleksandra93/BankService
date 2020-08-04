package repository;

import model.BankAccount;
import request.CreateBankAccountRequest;

import java.math.BigDecimal;
import java.util.*;

public class InMemoryBankAccountRepository implements BankAccountRepository {

    private List<BankAccount> bankAccounts = new ArrayList<>();

    private static InMemoryBankAccountRepository repository;

    //singleton
    public static InMemoryBankAccountRepository getInstance() {
        if (repository == null) {
            repository = new InMemoryBankAccountRepository();
        }
        return repository;
    }

    @Override
    public boolean existByPesel(String pesel) {
        return bankAccounts.stream()
                .anyMatch(bankAccount -> bankAccount.getPesel().equalsIgnoreCase(pesel));
    }

    @Override
    public BankAccount create(CreateBankAccountRequest request) {
        Random random = new Random();
        Long accountNumber = (random.nextLong()) * 2;
        BankAccount bankAccount = BankAccount.builder()
                .pesel(request.getPesel())
                .value(request.getInitialValue())
                .accountNumber(accountNumber.toString())
                .build();
        bankAccounts.add(bankAccount);
        return bankAccount;
    }

    @Override
    public List<BankAccount> findAll() {
        return Collections.unmodifiableList(bankAccounts);
    }

    @Override
    public void delete(String pesel) {
        bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getPesel().equals(pesel))
                .findFirst()
                .ifPresent(bankAccount -> bankAccounts.remove(bankAccount));

    }


    @Override
    public void addIncome(String pesel, BigDecimal income) {
        bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getPesel().equals(pesel))
                .findFirst()
                .ifPresent(bankAccount -> bankAccount.addMoney(income));

    }

       @Override
       public BankAccount findUsersBankAccountForGivenPesel(String pesel) {

           BankAccount bankAccount1 = bankAccounts.stream()
                   .filter(bankAccount -> bankAccount.getPesel().equals(pesel))
                   .findFirst()
                   .orElse(null);

           return bankAccount1;

       }


    @Override

    public boolean checkIfThereIsEnoughMoneyToDeductExpense(String pesel, BigDecimal expense) {
         BankAccount usersBankAccount = repository.findUsersBankAccountForGivenPesel(pesel);
         BigDecimal usersBankAccountValue = usersBankAccount.getValue();
         if (usersBankAccountValue.compareTo(expense)>=0){
             return true;
         }
         return false;

    }

    @Override
    public void deductMoney(String pesel, BigDecimal expense) {
        bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getPesel().equals(pesel))
                .findFirst()
                .ifPresent(bankAccount -> bankAccount.deductMoney(expense));

    }

}



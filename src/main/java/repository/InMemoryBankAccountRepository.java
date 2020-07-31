package repository;

import model.BankAccount;
import request.CreateBankAccountRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        Long accountNumber = (random.nextLong())*2;
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
}
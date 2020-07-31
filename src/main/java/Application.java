import model.BankAccount;
import repository.InMemoryBankAccountRepository;
import request.CreateBankAccountRequest;
import service.BankAccountService;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

public class Application {
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String THREE = "3";
    private static final String FOUR = "4";
    private static final String FIVE = "5";
    private static final String SIX = "6";
    private static final String NINE = "9";

    public static void main(String[] args) {

        BankAccountService service = new BankAccountService(InMemoryBankAccountRepository.getInstance());
        System.out.println("Hello");
        printMainMenu();

        Scanner scanner = new Scanner(System.in);
        String usersChoice = scanner.next();

        while (usersChoice.equals(ONE) || usersChoice.equals(TWO) || usersChoice.equals(THREE) || usersChoice.equals(FOUR) || usersChoice.equals(FIVE) || usersChoice.equals(SIX) || usersChoice.equals(NINE)) {
            if (usersChoice.equals(ONE)) {
                createBankAccount(service, scanner);

            } else if (usersChoice.equals(TWO)) {
                showAllBankAccounts(service);
            } else if (usersChoice.equals(THREE)) {
                showBankAccountForGivenPesel(service, scanner);
            } else if (usersChoice.equals(THREE)) {

            }else if (usersChoice.equals(FOUR)) {

            }else if (usersChoice.equals(FIVE)) {

            }else if (usersChoice.equals(SIX)) {

            }
            else if (usersChoice.equals(NINE)) {
                break;
            }

            System.out.println("---------------------");

            printMainMenu();
            usersChoice = scanner.next();


        }
    }

    public static void printMainMenu() {

        System.out.println("\nChoose what you want to do:\n");
        System.out.println("1. Create bank account - press 1");
        System.out.println("2. Show all accounts - press 2");
        System.out.println("3. Show bank account for particular pesel - press 3");
        System.out.println("4. Delete bank account - press 4");
        System.out.println("5. Add income into bank account - press 5");
        System.out.println("6. Make payment (decrease your bank account value) - press 6");
        System.out.println("\nif you want to exit, please press 9");
    }


    public static boolean checkIfPeselIsNotValid(String pesel) {
        int peselLenght = pesel.length();

        return (peselLenght != 11);
    }

    public static void createBankAccount(BankAccountService service, Scanner scanner) {
        System.out.println("please insert pesel: ");
        String pesel = scanner.next();
        while (checkIfPeselIsNotValid(pesel)) {
            System.out.println("entered pesel is not valid");
            System.out.println("please insert pesel: ");
            pesel = scanner.next();
        }
        ;
        CreateBankAccountRequest request = new CreateBankAccountRequest(pesel, BigDecimal.ZERO);
        Optional<BankAccount> bankAccount = service.createBankAccount(request);
        if (bankAccount.isEmpty()) {
            System.out.println("Cannot create bank account for given pesel. Bank account for given pesel have been already created");
        } else {
            System.out.println("Bank account created");
        }

    }

    public static void showAllBankAccounts(BankAccountService service) {
        List<BankAccount> bankAccounts = service.findAll();
        bankAccounts.forEach(bankAccount -> {
            System.out.println("Pesel: " + bankAccount.getPesel());
            System.out.println("Bank account number: " + bankAccount.getAccountNumber());
            System.out.println("Bank account value: " + bankAccount.getValue());
            System.out.println();
        });

    }

    public static void showBankAccountForGivenPesel(BankAccountService service, Scanner scanner) {
        System.out.println("please provide pesel: ");
        String pesel = scanner.next();
        List<BankAccount> bankAccounts = service.findAll();
        bankAccounts.stream()
                .filter(bankAccount -> bankAccount.getPesel().equalsIgnoreCase(pesel))
                .forEach(bankAccount -> {
                    System.out.println("Pesel: " + bankAccount.getPesel());
                    System.out.println("Bank account number: " + bankAccount.getAccountNumber());
                    System.out.println("Bank account value: " + bankAccount.getValue());
                    System.out.println();
                });

    }
}

import java.util.HashMap;
import java.util.Scanner;
public class BankManagementSystem {

    public static void main(String[] args) {
        HashMap<Integer, Account> accounts = new HashMap<>();   // account number -> account
        Scanner sc = new Scanner(System.in);
        int nextAccNo = 1001;
        int choice;

        do {
            System.out.println("\n===== BANK SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice == 1) {
                sc.nextLine();
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                System.out.print("Type (1 = Savings, 2 = Current): ");
                int type = sc.nextInt();
                System.out.print("Opening balance: ");
                double balance = sc.nextDouble();

                Account acc;
                if (type == 1) {
                    acc = new SavingsAccount(nextAccNo, name, balance);
                } else {
                    acc = new CurrentAccount(nextAccNo, name, balance);
                }
                accounts.put(nextAccNo, acc);
                System.out.println("Account created. Your account number is " + nextAccNo);
                nextAccNo++;

            } else if (choice >= 2 && choice <= 4) {
                System.out.print("Enter account number: ");
                Account acc = accounts.get(sc.nextInt());

                if (acc == null) {
                    System.out.println("Account not found.");
                } else if (choice == 2) {
                    System.out.print("Amount to deposit: ");
                    acc.deposit(sc.nextDouble());
                } else if (choice == 3) {
                    System.out.print("Amount to withdraw: ");
                    try {
                        acc.withdraw(sc.nextDouble());
                    } catch (InsufficientBalanceException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } else {
                    acc.showDetails();
                }

            } else if (choice != 0) {
                System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        System.out.println("Thank you!");
        sc.close();
    }
}

// Our own exception, used when the balance is too low
class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}

// Parent class: common things for every account
class Account {
    private int accountNumber;     // private = encapsulation
    private String name;
    private double balance;

    public Account(int accountNumber, String name, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = balance;
    }

    public double getMinimumBalance() {
        return 0;
    }

    public String getType() {
        return "Account";
    }

    public void deposit(double amount) {
        balance = balance + amount;
        System.out.println("Deposited. New balance: Rs." + balance);
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (balance - amount < getMinimumBalance()) {
            throw new InsufficientBalanceException(
                "Not enough balance. Minimum balance is Rs." + getMinimumBalance());
        }
        balance = balance - amount;
        System.out.println("Withdrawn. New balance: Rs." + balance);
    }

    public void showDetails() {
        System.out.println("Account No: " + accountNumber);
        System.out.println("Name      : " + name);
        System.out.println("Type      : " + getType());
        System.out.println("Balance   : Rs." + balance);
    }
}

// Child class 1 (inheritance): Savings must keep Rs.500
class SavingsAccount extends Account {
    public SavingsAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    public double getMinimumBalance() {
        return 500;
    }

    public String getType() {
        return "Savings";
    }
}

// Child class 2 (inheritance): Current has no minimum balance
class CurrentAccount extends Account {
    public CurrentAccount(int accNo, String name, double balance) {
        super(accNo, name, balance);
    }

    public String getType() {
        return "Current";
    }
}

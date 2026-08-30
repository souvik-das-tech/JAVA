// Q: Write a BankAccount class with a private double balance. Add a public
// getBalance() getter, a deposit(double amount) that rejects amounts <= 0,
// and a withdraw(double amount) that rejects amounts > current balance or
// <= 0. In main, exercise all three including the rejection cases.

class Account {
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        if (amount > balance) throw new IllegalArgumentException("insufficient funds");
        balance -= amount;
    }
}

public class BankAccount {
    public static void main(String[] args) {
        Account acc = new Account();
        acc.deposit(100);
        System.out.println("Balance after deposit: " + acc.getBalance());

        acc.withdraw(40);
        System.out.println("Balance after withdraw: " + acc.getBalance());

        try {
            acc.deposit(-10);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected deposit: " + e.getMessage());
        }

        try {
            acc.withdraw(1000);
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected withdraw: " + e.getMessage());
        }
    }
}

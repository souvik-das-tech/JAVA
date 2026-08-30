// Q: Create a checked InsufficientFundsException (extends Exception) and use
// it in a BankAccount.withdraw(double) method that throws it when the amount
// exceeds the balance. Create an unchecked InvalidAgeException (extends
// RuntimeException) thrown from a setAge(int) method for negative ages. In
// main, exercise both: catch InsufficientFundsException around a withdraw
// call, and trigger InvalidAgeException without a try/catch to see it
// propagate.

class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidAgeException extends RuntimeException {
    public InvalidAgeException(String message) {
        super(message);
    }
}

class BankAccount {
    private double balance;

    BankAccount(double balance) {
        this.balance = balance;
    }

    void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Need " + (amount - balance) + " more");
        }
        balance -= amount;
    }
}

class Person {
    int age;

    void setAge(int age) {
        if (age < 0) {
            throw new InvalidAgeException("Age cannot be negative: " + age);
        }
        this.age = age;
    }
}

public class CustomExceptionsDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(100);
        try {
            account.withdraw(500);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        Person person = new Person();
        person.setAge(-5); // uncaught InvalidAgeException propagates and terminates the program
    }
}

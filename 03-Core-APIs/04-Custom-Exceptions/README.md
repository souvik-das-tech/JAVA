# Custom Exceptions

Create your own exception type by extending `Exception` (checked) or `RuntimeException` (unchecked), to represent domain-specific error conditions with a meaningful name and optional extra data.

```java
class InsufficientFundsException extends Exception {          // checked
    public InsufficientFundsException(String message) {
        super(message);
    }
}

class InvalidAgeException extends RuntimeException {          // unchecked
    public InvalidAgeException(String message) {
        super(message);
    }
}
```

- Extend `Exception` when callers should be *forced* to acknowledge and handle the condition (compiler-enforced, via `throws`/`catch`) — typically for expected, recoverable business errors.
- Extend `RuntimeException` when the condition represents a programming/validation error that shouldn't require every caller up the chain to explicitly declare/catch it.
- Always call `super(message)` (or `super(message, cause)` to chain an underlying exception) so `getMessage()` and stack traces behave normally.
- Adding extra fields (e.g. `InsufficientFundsException` carrying the shortfall amount) lets catching code programmatically react, not just log a message.

```java
class BankAccount {
    void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Need " + (amount - balance) + " more");
        }
        balance -= amount;
    }
}
```

## Practice Questions / Exercises

- Create a checked `InsufficientFundsException` and use it in a `BankAccount.withdraw()` method — write a caller that catches it and prints the message.
- Create an unchecked `InvalidAgeException` and throw it from a `setAge(int)` method when age is negative — show it doesn't require a `throws` declaration or a `try/catch` at the call site (though it can still be caught).
- Add an extra field to a custom exception (e.g. `double shortfall`) with a getter, and use it in a `catch` block to print a custom message using that field.
- Chain a custom exception to an underlying cause: `throw new MyException("wrapped", originalException);` and print the cause via `getCause()`.

## Interview Questions

**Q: When would you create a custom checked exception vs a custom unchecked exception?**
A: Checked, when you want the compiler to force every caller to explicitly handle or propagate the condition — appropriate for expected, recoverable business-level failures the caller should plan for (e.g. insufficient funds). Unchecked, when the condition represents a violated precondition/programming error that shouldn't burden every intermediate caller with a `throws` declaration (e.g. invalid input to a validation method).

**Q: Why should a custom exception's constructor call `super(message)`?**
A: `Throwable` stores the message and builds the stack trace internally; calling `super(message)` (or `super(message, cause)`) ensures `getMessage()`, `printStackTrace()`, and logging frameworks that inspect the exception all work correctly, instead of the custom exception's message being lost or inaccessible.

**Q: What's the benefit of a custom exception over throwing a generic `RuntimeException("some message")`?**
A: A named custom exception type lets calling code catch and react to that *specific* condition (via its type, not by parsing a message string), can carry structured extra data relevant to the failure (e.g. the exact shortfall amount), and makes stack traces/logs immediately self-descriptive about what went wrong.

**Q: What is exception chaining, and why use it?**
A: Wrapping a lower-level exception inside a higher-level, more meaningful one while preserving the original as the "cause" (`new MyException("context", originalException)`, retrievable via `getCause()`). It lets you translate a low-level failure (e.g. a raw `SQLException`) into a domain-specific one (e.g. `UserNotFoundException`) without losing the original diagnostic information.

**Q: If a custom exception extends `RuntimeException`, can it still be explicitly caught with `catch`?**
A: Yes — "unchecked" only means the compiler doesn't *force* callers to catch or declare it; it can absolutely still be caught explicitly (e.g. `catch (InvalidAgeException e)`) wherever the caller chooses to handle it, exactly like a checked exception.

**Q: Is it acceptable for a custom exception to extend `Error` instead of `Exception`?**
A: Generally no — `Error` is reserved for serious, typically unrecoverable JVM/environment-level conditions (`OutOfMemoryError`, etc.) that application code isn't expected to catch. Application-level custom exceptions should extend `Exception` or `RuntimeException` so they fit the conventional catch/handle model.

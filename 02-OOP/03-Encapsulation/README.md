# Encapsulation

Encapsulation means bundling data (fields) with the methods that operate on it, and restricting direct access to that data from outside the class — typically by making fields `private` and exposing controlled access via public getters/setters.

```java
class BankAccount {
    private double balance;   // hidden — can't be touched directly from outside

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
        balance += amount;
    }
}
```

- Direct field access (`account.balance = -500;`) would let external code put the object into an invalid state; a setter/method can validate first.
- A getter doesn't have to just return the field — it can compute a derived value. A setter doesn't have to just assign — it can validate, transform, or reject.
- Encapsulation is about hiding **implementation details**, not just hiding data: you can change how a class stores/computes something internally without breaking code that uses it, as long as the public method signatures stay the same.

## Access modifiers (quick reference)

| Modifier | Same class | Same package | Subclass (diff. package) | Everywhere |
|---|---|---|---|---|
| `private` | ✅ | ❌ | ❌ | ❌ |
| default (none) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

## Practice Questions / Exercises

- Write a `BankAccount` class with a `private double balance`, a `deposit(double)` method that rejects non-positive amounts, and a `withdraw(double)` method that rejects amounts greater than the current balance.
- Make `balance` accessible only via a getter (no setter) — show that this makes the field effectively read-only from outside the class.
- Add a computed getter, e.g. `isOverdrawn()` returning `balance < 0`, that isn't backed by a stored field.
- Demonstrate what happens if you make `balance` `public` instead — write code from outside the class that puts the account into an invalid state (e.g. negative balance) directly.

## Interview Questions

**Q: What is encapsulation, and why is it useful?**
A: Encapsulation is bundling an object's data with the methods that operate on it, and restricting direct outside access to that data (usually via `private` fields + public getters/setters). It's useful because it lets a class validate/control how its state changes, and lets you change the internal implementation later without breaking external code that only depends on the public method signatures.

**Q: Is encapsulation the same as just making fields private and adding getters/setters for all of them?**
A: Not exactly — mechanically adding a getter and setter for every private field (an "anemic" class) doesn't really encapsulate anything if the setter does no validation, since it's functionally identical to a public field. True encapsulation is about controlling *how* state can change, which sometimes means no setter at all, or a setter that validates or transforms input.

**Q: What's the difference between `private`, default (package-private), `protected`, and `public`?**
A: `private` — accessible only within the declaring class. Default (no modifier) — accessible within the same package. `protected` — accessible within the same package plus subclasses in other packages. `public` — accessible from anywhere.

**Q: Can a class be encapsulated without using `private` fields at all?**
A: Practically, no — if fields are package-private, protected, or public, any code with sufficient access can mutate them directly, bypassing any validation logic in setters. `private` is what actually enforces that all access goes through the class's own methods.

**Q: How does encapsulation support immutability?**
A: An immutable class (e.g. `String`) takes encapsulation further: `private final` fields set only in the constructor, no setters at all, and no methods that expose a mutable internal reference. Since nothing outside (or even inside, after construction) can change the state, instances are safe to share freely, including across threads.

**Q: Why is exposing a mutable field via a getter still a potential encapsulation leak, even if the field itself is `private`?**
A: If a getter returns a direct reference to a mutable internal object (like a `List` or `Date`), the caller can mutate that object's contents through the returned reference, bypassing the class entirely — the field is technically private, but its state isn't actually protected. Fixing this usually means returning a defensive copy or an unmodifiable view.

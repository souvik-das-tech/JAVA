// Q: Declare a final int MAX_USERS = 100 and a final int[] scores = {1,2,3}.
// Show (in comments or by writing code that would fail) that MAX_USERS
// cannot be reassigned, but scores[0] can be mutated while `scores` itself
// cannot be reassigned to a new array. Then write a class Config with a
// final String name field assigned via its constructor.

class Config {
    final String name;

    Config(String name) {
        this.name = name;
    }
}

public class FinalKeywordDemo {
    public static void main(String[] args) {
        final int MAX_USERS = 100;
        // MAX_USERS = 200; // compile error: cannot assign a value to final variable MAX_USERS
        System.out.println("MAX_USERS = " + MAX_USERS);

        final int[] scores = { 1, 2, 3 };
        scores[0] = 99; // OK — mutating array contents is allowed
        // scores = new int[5]; // compile error: cannot assign a value to final variable scores
        System.out.println("scores[0] = " + scores[0]);

        Config config = new Config("prod");
        System.out.println("Config name = " + config.name);
    }
}

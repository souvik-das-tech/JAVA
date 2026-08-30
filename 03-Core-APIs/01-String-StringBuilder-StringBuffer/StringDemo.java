// Q: Show that two string literals with equal content are == equal, but
// `new String(...)` with the same content is not, while .equals() is true
// for both. Then write a palindrome checker using StringBuilder.reverse(),
// and test it on a few strings including a non-palindrome.

public class StringDemo {
    static boolean isPalindrome(String s) {
        String reversed = new StringBuilder(s).reverse().toString();
        return s.equals(reversed);
    }

    public static void main(String[] args) {
        String a = "hello";
        String b = "hello";
        String c = new String("hello");

        System.out.println("a == b: " + (a == b));
        System.out.println("a == c: " + (a == c));
        System.out.println("a.equals(c): " + a.equals(c));

        String[] words = { "level", "world", "racecar", "java" };
        for (String w : words) {
            System.out.println(w + " -> palindrome? " + isPalindrome(w));
        }
    }
}

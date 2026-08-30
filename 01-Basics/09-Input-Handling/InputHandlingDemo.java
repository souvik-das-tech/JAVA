import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHandlingDemo {
    public static void main(String[] args) throws IOException {
        // --- Scanner: nextInt() then nextLine() pitfall, then the fix ---
        Scanner sc = new Scanner(System.in);
        System.out.println("-- Scanner demo --");
        System.out.print("Enter your age: ");
        int age = sc.nextInt();
        sc.nextLine(); // consume the leftover newline left by nextInt() -- the fix
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        System.out.println("Scanner read -> age: " + age + ", name: " + name);

        // Scanner: sum space-separated numbers on one line
        // (no extra nextLine() needed here - the previous nextLine() for `name`
        // already consumed through the end of that line)
        System.out.print("Enter numbers separated by spaces: ");
        String numberLine = sc.nextLine();
        int sumScanner = 0;
        Scanner lineScanner = new Scanner(numberLine);
        while (lineScanner.hasNextInt()) {
            sumScanner += lineScanner.nextInt();
        }
        System.out.println("Sum (Scanner): " + sumScanner);

        // Scanner: handle invalid input gracefully
        System.out.print("Enter a number (try typing text to see the error handled): ");
        try {
            int value = sc.nextInt();
            System.out.println("You entered: " + value);
        } catch (InputMismatchException e) {
            System.out.println("Invalid number, caught: " + e);
            sc.next(); // discard the unparsed token so it doesn't corrupt later reads
        }
        sc.close();

        // --- BufferedReader: manual parsing ---
        // Using canned input (StringReader) here instead of continuing to read
        // System.in: Scanner buffers input internally, so once a Scanner has
        // been reading from System.in, a fresh BufferedReader wrapped around
        // the same stream can miss bytes Scanner already pulled into its own
        // buffer. In a real program you pick ONE of Scanner/BufferedReader for
        // all input on a given stream - never mix them on the same source.
        String cannedInput = "30\nBob\n10 20 30\n";
        BufferedReader br = new BufferedReader(new StringReader(cannedInput));
        System.out.println("-- BufferedReader demo (reading canned input) --");
        int age2 = Integer.parseInt(br.readLine());
        String name2 = br.readLine();
        System.out.println("BufferedReader read -> age: " + age2 + ", name: " + name2);

        // BufferedReader: sum space-separated numbers on one line
        String[] tokens = br.readLine().trim().split("\\s+");
        int sumReader = 0;
        for (String token : tokens) {
            sumReader += Integer.parseInt(token);
        }
        System.out.println("Sum (BufferedReader): " + sumReader);
        br.close();

        // To read real console input with BufferedReader instead, you'd do:
        // BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
}

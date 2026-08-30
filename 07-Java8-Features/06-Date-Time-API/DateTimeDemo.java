// Q: Create a LocalDate for a birthday (e.g. 2000-05-15), and compute the
// age in years using Period.between(birthday, LocalDate.now()).getYears().
// Compute the Duration between two LocalTimes (09:00 and 17:30), printing
// total hours and minutes. Format a LocalDate using a custom pattern
// (dd-MM-yyyy) and parse it back. Demonstrate immutability by calling
// .plusDays(5) on a LocalDate without reassigning, showing the original is
// unchanged.

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateTimeDemo {
    public static void main(String[] args) {
        LocalDate birthday = LocalDate.of(2000, 5, 15);
        Period age = Period.between(birthday, LocalDate.now());
        System.out.println("Age in years: " + age.getYears());

        Duration workDay = Duration.between(LocalTime.of(9, 0), LocalTime.of(17, 30));
        System.out.println("Work day hours: " + workDay.toHours());
        System.out.println("Work day minutes: " + workDay.toMinutes());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatted = birthday.format(fmt);
        System.out.println("Formatted: " + formatted);
        LocalDate parsed = LocalDate.parse(formatted, fmt);
        System.out.println("Parsed back: " + parsed);

        LocalDate original = LocalDate.of(2026, 1, 1);
        original.plusDays(5); // result discarded — original is unchanged
        System.out.println("Original unchanged: " + original);
        LocalDate shifted = original.plusDays(5);
        System.out.println("Shifted (new instance): " + shifted);
    }
}

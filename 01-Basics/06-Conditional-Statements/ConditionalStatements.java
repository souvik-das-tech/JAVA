public class ConditionalStatements {
    public static void main(String[] args) {
        // if / else if / else -> letter grade
        int score = 82;
        String grade;
        if (score >= 90) {
            grade = "A";
        } else if (score >= 75) {
            grade = "B";
        } else {
            grade = "C";
        }
        System.out.println("Score " + score + " -> grade " + grade);

        // Classic switch on int month -> season, WITH fall-through demonstrated then fixed
        int fallThroughMonth = 4; // April: lands in the case with the missing break
        System.out.println("-- classic switch (with a missing break, showing fall-through) --");
        switch (fallThroughMonth) {
            case 12:
            case 1:
            case 2:
                System.out.println("Winter");
                break;
            case 3:
            case 4:
            case 5:
                System.out.println("Spring");
                // intentionally missing break to show fall-through
            case 6:
            case 7:
            case 8:
                System.out.println("Summer"); // prints too, because Spring fell through
                break;
            default:
                System.out.println("Autumn");
        }

        int month = 6;
        System.out.println("-- fixed classic switch --");
        switch (month) {
            case 12: case 1: case 2:
                System.out.println("Winter");
                break;
            case 3: case 4: case 5:
                System.out.println("Spring");
                break;
            case 6: case 7: case 8:
                System.out.println("Summer");
                break;
            default:
                System.out.println("Autumn");
        }

        // Arrow-form switch expression, same logic
        String season = switch (month) {
            case 12, 1, 2 -> "Winter";
            case 3, 4, 5 -> "Spring";
            case 6, 7, 8 -> "Summer";
            default -> "Autumn";
        };
        System.out.println("Arrow-form switch season: " + season);

        // switch on String with grouped case labels
        String day = "Saturday";
        String type = switch (day) {
            case "Saturday", "Sunday" -> "Weekend";
            default -> "Weekday";
        };
        System.out.println(day + " is a " + type);

        // nested if: classify positive/negative and even/odd
        int number = -14;
        if (number >= 0) {
            if (number % 2 == 0) {
                System.out.println(number + " is positive and even");
            } else {
                System.out.println(number + " is positive and odd");
            }
        } else {
            if (number % 2 == 0) {
                System.out.println(number + " is negative and even");
            } else {
                System.out.println(number + " is negative and odd");
            }
        }
    }
}

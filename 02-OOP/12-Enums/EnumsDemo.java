// Q: Write a Day enum with all 7 days. In main, loop over Day.values() and
// use a switch to print "Weekend" for SATURDAY/SUNDAY and "Weekday" for the
// rest, also printing each constant's ordinal(). Then write a Planet enum
// with mass and radius fields set via its constructor and a
// surfaceGravity() method, and print the gravity for two planets.

enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

enum Planet {
    MERCURY(3.3e23, 2.4e6),
    EARTH(5.9e24, 6.4e6);

    private final double mass, radius;

    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    double surfaceGravity() {
        return 6.674e-11 * mass / (radius * radius);
    }
}

public class EnumsDemo {
    public static void main(String[] args) {
        for (Day d : Day.values()) {
            String kind;
            switch (d) {
                case SATURDAY:
                case SUNDAY:
                    kind = "Weekend";
                    break;
                default:
                    kind = "Weekday";
            }
            System.out.println(d + " (ordinal " + d.ordinal() + ") -> " + kind);
        }

        for (Planet p : Planet.values()) {
            System.out.println(p + " surface gravity = " + p.surfaceGravity());
        }
    }
}

// Q: Create a Car class with fields `model` (String) and `speed` (int), and a
// method `accelerate()` that increases speed by 10. In main, create two Car
// objects with different models, call accelerate() on one of them, and print
// both objects' state to show their instance data is independent.

class Car {
    String model;
    int speed;

    void accelerate() {
        speed += 10;
    }

    void printDetails() {
        System.out.println(model + " -> speed: " + speed);
    }
}

public class ClassesAndObjects {
    public static void main(String[] args) {
        Car c1 = new Car();
        c1.model = "Civic";
        Car c2 = new Car();
        c2.model = "Model 3";

        c1.accelerate();
        c1.accelerate();

        c1.printDetails();
        c2.printDetails();
    }
}

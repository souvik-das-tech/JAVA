// Q: Write a Drivable interface with an abstract drive() method and a default
// honk() method that prints "Beep!". Implement Drivable in Car and Bike
// classes (each with its own drive() message). In main, put a Car and a Bike
// into a Drivable[] array and call drive() and honk() on each.

interface Drivable {
    void drive();

    default void honk() {
        System.out.println("Beep!");
    }
}

class Car implements Drivable {
    @Override
    public void drive() {
        System.out.println("Car is driving on the road");
    }
}

class Bike implements Drivable {
    @Override
    public void drive() {
        System.out.println("Bike is pedaling along the path");
    }
}

public class InterfacesDemo {
    public static void main(String[] args) {
        Drivable[] vehicles = { new Car(), new Bike() };
        for (Drivable v : vehicles) {
            v.drive();
            v.honk();
        }
    }
}

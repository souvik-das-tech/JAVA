// Q: Write an Animal class with a sound() method printing "...". Write Cat
// and Dog subclasses overriding sound() to print "Meow" and "Woof". In main,
// create an Animal[] array holding a Cat and a Dog, and loop over it calling
// sound() on each element to show runtime dispatch picks the right override.

class Animal {
    void sound() {
        System.out.println("...");
    }
}

class Cat extends Animal {
    @Override
    void sound() {
        System.out.println("Meow");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Woof");
    }
}

public class PolymorphismDemo {
    public static void main(String[] args) {
        Animal[] animals = { new Cat(), new Dog() };
        for (Animal a : animals) {
            a.sound();
        }
    }
}

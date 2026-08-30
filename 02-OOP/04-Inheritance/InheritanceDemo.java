// Q: Write an Animal class with a String name field, a constructor that sets
// it, and an eat() method that prints "<name> is eating". Write a Dog class
// that extends Animal, adds a bark() method, and overrides eat() to call
// super.eat() and then print an extra line. In main, create a Dog and call
// eat() and bark() on it.

class Animal {
    String name;

    Animal(String name) {
        this.name = name;
    }

    void eat() {
        System.out.println(name + " is eating");
    }
}

class Dog extends Animal {
    Dog(String name) {
        super(name);
    }

    void bark() {
        System.out.println(name + " says Woof");
    }

    @Override
    void eat() {
        super.eat();
        System.out.println("...enthusiastically");
    }
}

public class InheritanceDemo {
    public static void main(String[] args) {
        Dog d = new Dog("Rex");
        d.eat();
        d.bark();
    }
}

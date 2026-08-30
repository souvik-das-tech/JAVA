// Q: Write an Employee class (name, age, salary, department) implementing
// Comparable<Employee> by age. Sort a List<Employee> using
// Collections.sort(list) (natural order). Then write two Comparators (by
// name, by salary descending) and sort the same list both ways without
// touching Employee. Finally chain Comparator.comparing(department)
// .thenComparing(name) and sort by that.

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Employee implements Comparable<Employee> {
    String name;
    int age;
    double salary;
    String department;

    Employee(String name, int age, double salary, String department) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.department = department;
    }

    @Override
    public int compareTo(Employee other) {
        return Integer.compare(this.age, other.age);
    }

    @Override
    public String toString() {
        return name + "(" + age + ", $" + salary + ", " + department + ")";
    }
}

public class ComparableComparatorDemo {
    public static void main(String[] args) {
        List<Employee> employees = new java.util.ArrayList<>(List.of(
                new Employee("Charlie", 35, 90000, "Engineering"),
                new Employee("Alice", 28, 75000, "Marketing"),
                new Employee("Bob", 42, 60000, "Engineering"),
                new Employee("Dana", 28, 95000, "Marketing")
        ));

        Collections.sort(employees);
        System.out.println("Sorted by age (natural order): " + employees);

        Comparator<Employee> byName = (e1, e2) -> e1.name.compareTo(e2.name);
        employees.sort(byName);
        System.out.println("Sorted by name: " + employees);

        Comparator<Employee> bySalaryDesc = Comparator.comparingDouble((Employee e) -> e.salary).reversed();
        employees.sort(bySalaryDesc);
        System.out.println("Sorted by salary desc: " + employees);

        Comparator<Employee> byDeptThenName = Comparator
                .comparing((Employee e) -> e.department)
                .thenComparing(e -> e.name);
        employees.sort(byDeptThenName);
        System.out.println("Sorted by department then name: " + employees);
    }
}

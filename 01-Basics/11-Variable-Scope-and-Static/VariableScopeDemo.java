public class VariableScopeDemo {

    static class Counter {
        static int totalCount = 0; // shared across all instances
        int id;                     // separate per instance

        Counter() {
            int localTemp = 5; // local: only exists during this constructor call
            id = ++totalCount + localTemp - localTemp; // localTemp used, then gone
        }
    }

    static final String VERSION;
    static {
        // static block: runs once, when the class is first loaded
        VERSION = "1.0.0";
    }

    public static void main(String[] args) {
        // static vs instance variables
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();
        System.out.println("c1.id=" + c1.id + " c2.id=" + c2.id + " c3.id=" + c3.id);
        System.out.println("Counter.totalCount (shared) = " + Counter.totalCount);

        // local variable scope: declared inside an if block, only usable inside it
        int outerScoped = 0; // must have a definite value before the if, since the
                              // compiler can't prove the "if" branch always runs
        if (Counter.totalCount > 0) {
            int insideIf = 42; // only visible within this block
            outerScoped = insideIf; // must copy it out before the block ends
            System.out.println("insideIf (inside block): " + insideIf);
        }
        System.out.println("value copied out of the block: " + outerScoped);
        // System.out.println(insideIf); // would NOT compile here - out of scope

        // static method calling an instance method requires an instance first
        callInstanceMethodProperly();

        // static initializer block ran once at class load time
        System.out.println("VERSION (set in static block) = " + VERSION);
    }

    void instanceGreet() {
        System.out.println("Hello from an instance method");
    }

    static void callInstanceMethodProperly() {
        // instanceGreet(); // would NOT compile: no implicit 'this' in a static context
        VariableScopeDemo instance = new VariableScopeDemo();
        instance.instanceGreet(); // must go through an actual object
    }
}

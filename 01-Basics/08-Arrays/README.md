# Arrays (1D, 2D, Multi-Dimensional)

## 1D arrays

```java
int[] nums = new int[5];        // all elements default to 0
int[] nums2 = {1, 2, 3, 4, 5};  // array literal
nums[0] = 10;                   // indices are 0-based
System.out.println(nums.length); // 5 - length is a field, not a method
```

- Arrays are **fixed-size** once created — size can't grow/shrink; `length` is a public final field, not a method call like `.length()` (that's for `String`).
- Arrays are objects, so an uninitialized array reference field defaults to `null`.

## 2D arrays

```java
int[][] grid = new int[3][4];       // 3 rows, 4 columns, all 0
int[][] grid2 = {{1, 2}, {3, 4}, {5, 6}};

for (int r = 0; r < grid2.length; r++) {
    for (int c = 0; c < grid2[r].length; c++) {
        System.out.print(grid2[r][c] + " ");
    }
}
```

- A Java 2D array is really an **array of arrays** — `grid[r]` is itself an `int[]`.

## Jagged arrays

Because a 2D array is an array of arrays, each row can have a **different length**:

```java
int[][] jagged = new int[3][];
jagged[0] = new int[]{1};
jagged[1] = new int[]{1, 2, 3};
jagged[2] = new int[]{1, 2};
```

## Multi-dimensional (3D+)

```java
int[][][] cube = new int[2][3][4]; // array of arrays of arrays
```

## Common utilities (`java.util.Arrays`)

```java
Arrays.toString(nums);      // "[1, 2, 3, 4, 5]" - for printing 1D arrays
Arrays.deepToString(grid2); // for printing nested/2D arrays
Arrays.sort(nums);
Arrays.fill(nums, 0);
Arrays.equals(a, b);        // element-wise equality
```

- Directly `System.out.println(nums)` prints something like `[I@1b6d3586` (type + hash), not the contents — always use `Arrays.toString`/`deepToString`.

## Practice Questions / Exercises

- Declare a 1D `int[]` array via literal, print it with `Arrays.toString`, then print it directly with `System.out.println` and observe the difference.
- Build a 2D array representing a 3x3 grid, fill it with the multiplication table, and print it row by row.
- Build a jagged array where row lengths increase (row 0 has 1 element, row 1 has 2, row 2 has 3) and print it with `Arrays.deepToString`.
- Write a method that takes an `int[]` and returns the max element without using any library method.
- Demonstrate `ArrayIndexOutOfBoundsException` by deliberately accessing an out-of-range index, in a try/catch.

## Interview Questions

**Q: Are arrays in Java fixed-size or resizable?**
A: Fixed-size — once created with `new int[5]`, its length can never change. To get resizable behavior you need something like `ArrayList` (from the Collections framework), which internally manages resizing by allocating a new backing array when needed.

**Q: How is a 2D array actually represented in Java?**
A: As an array of array references — `int[][] grid` is an array where each element `grid[i]` is itself a reference to a separate `int[]` object. This is why rows can have different lengths (jagged arrays), unlike a true rectangular matrix in some other languages.

**Q: Why does `System.out.println(myArray)` print something like `[I@1b6d3586` instead of the contents?**
A: Arrays don't override `toString()` from `Object`, so printing one directly calls the default `Object.toString()`, which prints the type descriptor (`[I` = array of `int`) plus the hash code. Use `Arrays.toString()` (1D) or `Arrays.deepToString()` (nested arrays) to print actual contents.

**Q: What is a jagged array, and why is it possible in Java but not, say, in a true 2D C array?**
A: A jagged array is a 2D (or higher) array where each row is an independently-sized array. It's possible because Java's multi-dimensional arrays are really arrays of array *references*, not one contiguous rectangular memory block — each row can point to a differently-sized array object.

**Q: What exception is thrown for an out-of-bounds array access, and when — compile time or runtime?**
A: `ArrayIndexOutOfBoundsException`, thrown at **runtime**. Array bounds aren't checked at compile time in general (except for obviously-constant cases), so accessing `arr[arr.length]` compiles fine but throws when executed.

**Q: What's the default value of elements in a newly created `int[]` vs `String[]`?**
A: `int[]` elements default to `0` (primitive numeric default). `String[]` elements default to `null`, since `String` is a reference type and the array itself doesn't create any `String` objects on allocation.

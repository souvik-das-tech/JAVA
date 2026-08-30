import java.util.Arrays;

public class ArraysDemo {
    public static void main(String[] args) {
        // 1D array literal, printed with Arrays.toString vs raw println
        int[] nums = {1, 2, 3, 4, 5};
        System.out.println("Arrays.toString: " + Arrays.toString(nums));
        System.out.println("raw println (type@hash, not contents): " + nums);

        // 2D array: 3x3 multiplication table
        int[][] grid = new int[3][3];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                grid[r][c] = (r + 1) * (c + 1);
            }
        }
        System.out.println("-- multiplication table --");
        for (int[] row : grid) {
            System.out.println(Arrays.toString(row));
        }

        // Jagged array: increasing row lengths
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1};
        jagged[1] = new int[]{1, 2};
        jagged[2] = new int[]{1, 2, 3};
        System.out.println("Jagged array: " + Arrays.deepToString(jagged));

        // Max element without library methods
        int[] values = {3, 7, 2, 9, 4};
        System.out.println("Max: " + max(values));

        // ArrayIndexOutOfBoundsException demo
        try {
            int outOfRange = values[values.length]; // one past the end
            System.out.println("unreachable: " + outOfRange);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Caught: " + e);
        }
    }

    private static int max(int[] arr) {
        int best = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > best) {
                best = arr[i];
            }
        }
        return best;
    }
}

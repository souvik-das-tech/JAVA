// Q: Write "Line 1", "Line 2", "Line 3" to a file called demo.txt using
// BufferedWriter (one per line via newLine()), inside a try-with-resources
// block. Then read the file back line-by-line using BufferedReader and print
// each line along with a running line count. Use a File object first to
// check whether demo.txt exists before writing to it.

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIODemo {
    public static void main(String[] args) {
        File file = new File("demo.txt");
        System.out.println("Exists before write? " + file.exists());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("Line 1");
            bw.newLine();
            bw.write("Line 2");
            bw.newLine();
            bw.write("Line 3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                count++;
                System.out.println(count + ": " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        file.delete();
    }
}

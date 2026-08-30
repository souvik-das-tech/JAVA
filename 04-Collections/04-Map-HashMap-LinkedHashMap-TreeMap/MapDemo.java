// Q: Given a sentence (String), split it into words and build a word
// frequency count using HashMap<String, Integer> and merge(). Print the
// result. Then insert the same words into a LinkedHashMap and a TreeMap and
// print each to compare iteration order. Finally build a
// Map<String, List<String>> grouping words by their first letter, using
// computeIfAbsent.

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapDemo {
    public static void main(String[] args) {
        String sentence = "the quick brown fox jumps over the lazy dog the fox runs";
        String[] words = sentence.split(" ");

        Map<String, Integer> frequency = new HashMap<>();
        for (String w : words) {
            frequency.merge(w, 1, Integer::sum);
        }
        System.out.println("Frequency (HashMap): " + frequency);

        Map<String, Integer> linkedFrequency = new LinkedHashMap<>();
        Map<String, Integer> treeFrequency = new TreeMap<>();
        for (String w : words) {
            linkedFrequency.merge(w, 1, Integer::sum);
            treeFrequency.merge(w, 1, Integer::sum);
        }
        System.out.println("Frequency (LinkedHashMap, insertion order): " + linkedFrequency);
        System.out.println("Frequency (TreeMap, sorted order): " + treeFrequency);

        Map<Character, List<String>> byFirstLetter = new HashMap<>();
        for (String w : words) {
            byFirstLetter.computeIfAbsent(w.charAt(0), k -> new ArrayList<>()).add(w);
        }
        System.out.println("Grouped by first letter: " + byFirstLetter);
    }
}

import java.io.*; // пакет (бібліотека), що містить класи, необхідні для введення та виведення даних
import java.util.*;

public class B0508 {
    private static class Box {
        int size;
        String color;
        String mat;
        public Box(int size, String color, String mat) {
            this.size = size;
            this.color = color;
            this.mat = mat;
        }
        @Override
        public String toString() {
            return String.format("size: %d centimeters, color: %s, material: %s", size, color, mat);
        }
    }
    private static final String IN_FILE = "cube_data.txt";  // змінна може бути ініціалізована лише один раз - final
    private static final String OUT_FILE_A = "cubes_by_size_result.txt";
    private static final String OUT_FILE_B = "cubes_by_color_count.txt";
    private static List<Box> readBoxes() {
        List<Box> boxes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(IN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())  // видалення пробілів - trim()
                    continue;
                String[] p = line.split("\\s+");  // використовується, щоб знайти або розділити рядок за будь-якою послідовністю пробілів, табуляцій або нових рядків, незалежно від їх к-сті
                if (p.length >= 3) {
                    try {
                        int size = Integer.parseInt(p[0]);
                        String color = p[1];
                        String mat = p[2];
                        boxes.add(new Box(size, color, mat));
                    } catch (NumberFormatException e) {
                        System.err.println("incorrect size format in line: " + line);
                    }
                } else {
                    System.err.println("incorrect line format (not enough fields): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("input file " + IN_FILE + " not found");
            System.err.println("create a log file");
        } catch (IOException e) {
            System.err.println("can't read " + e.getMessage());
        }
        return boxes;
    }
    
    public static void findBySize(List<Box> boxes, int targetSize) throws IOException {
        int count = 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(OUT_FILE_A))) {
            pw.println("---search results for cubes with size " + targetSize + " centimeters---");
            for (Box box : boxes) {
                if (box.size == targetSize) {
                    pw.println(box.toString());
                    count++;
                }
            }
            if (count == 0) {
                pw.println("not found");
            } else {
                pw.println("all found: " + count + " cubes");
            }
            System.out.println("✅ the result of the search by size is saved in the file: " + OUT_FILE_A);
        } 
    }

    public static void countByColor(List<Box> boxes) throws IOException {
        Map<String, Integer> counts = new HashMap<>();
        for (Box box : boxes) {
            String color = box.color.toLowerCase();
            counts.put(color, counts.getOrDefault(color, 0) + 1);
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(OUT_FILE_B))) {
            pw.println("---number of cubes of each color---");

            for (Map.Entry<String, Integer> entry : counts.entrySet()) {
                pw.println(String.format("%s: %d",
                        entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), 
                        entry.getValue()));
            }
            pw.println("everything in the file is processed: " + boxes.size() + " records");
            System.out.println("✅ the result of counting by colors is recorded in the file:" + OUT_FILE_B);
        } 
    }

    public static void main(String[] args) {
        List<Box> boxes = readBoxes();
        if (boxes.isEmpty()) {
            System.out.println("\nno data to process, complete");
            return;
        }
        int sizeToFind = 10;
        try {
            findBySize(boxes, sizeToFind);
        } catch (IOException e) {
            System.err.println("error when writing to file A: " + e.getMessage());
        }
        try {
            countByColor(boxes);
        } catch (IOException e) {
            System.err.println("error when writing to file B: " + e.getMessage());
        }
    }
}

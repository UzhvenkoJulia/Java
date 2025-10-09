import java.io.*; // пакет (бібліотека), що містить класи, необхідні для введення та виведення даних
import java.util.*;

public class B0508 {
    private static class Cube {
        int size;
        String color;
        String material;
        public Cube(int size, String color, String material) {
            this.size = size;
            this.color = color;
            this.material = material;
        }
        @Override
        public String toString() {
            return String.format("size: %d centimeters, color: %s, material: %s", size, color, material);
        }
    }
    private static final String INPUT_FILE = "cube_data.txt";  // змінна може бути ініціалізована лише один раз - final
    private static final String OUTPUT_FILE_A = "cubes_by_size_result.txt";
    private static final String OUTPUT_FILE_B = "cubes_by_color_count.txt";
    private static List<Cube> readCubesFromFile() {
        List<Cube> cubes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty())  // видалення пробілів - trim()
                    continue;
                String[] parts = line.split("\\s+");  // використовується, щоб знайти або розділити рядок за будь-якою послідовністю пробілів, табуляцій або нових рядків, незалежно від їх к-сті
                if (parts.length >= 3) {
                    try {
                        int size = Integer.parseInt(parts[0]);
                        String color = parts[1];
                        String material = parts[2];
                        cubes.add(new Cube(size, color, material));
                    } catch (NumberFormatException e) {
                        System.err.println("incorrect size format in line: " + line);
                    }
                } else {
                    System.err.println("incorrect line format (not enough fields): " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("input file " + INPUT_FILE + " not found");
            System.err.println("create a log file");
        } catch (IOException e) {
            System.err.println("can't read " + e.getMessage());
        }
        return cubes;
    }
    
    public static void findCubesBySize(List<Cube> cubes, int targetSize) throws IOException {
        int count = 0;
        try (PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE_A))) {
            pw.println("---search results for cubes with size " + targetSize + " centimeters---");
            for (Cube cube : cubes) {
                if (cube.size == targetSize) {
                    pw.println(cube.toString());
                    count++;
                }
            }
            if (count == 0) {
                pw.println("not found");
            } else {
                pw.println("all found: " + count + " cubes");
            }
            System.out.println("✅ the result of the search by size is saved in the file: " + OUTPUT_FILE_A);
        } 
    }

    public static void countCubesByColor(List<Cube> cubes) throws IOException {
        Map<String, Integer> colorCounts = new HashMap<>();
        for (Cube cube : cubes) {
            String color = cube.color.toLowerCase();
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(OUTPUT_FILE_B))) {
            pw.println("---number of cubes of each color---");

            for (Map.Entry<String, Integer> entry : colorCounts.entrySet()) {
                pw.println(String.format("%s: %d",
                        entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), 
                        entry.getValue()));
            }
            pw.println("everything in the file is processed: " + cubes.size() + " records");
            System.out.println("✅ the result of counting by colors is recorded in the file:" + OUTPUT_FILE_B);
        } 
    }

    public static void main(String[] args) {
        List<Cube> cubes = readCubesFromFile();
        if (cubes.isEmpty()) {
            System.out.println("\nno data to process, complete");
            return;
        }
        int sizeToFind = 10;
        try {
            findCubesBySize(cubes, sizeToFind);
        } catch (IOException e) {
            System.err.println("error when writing to file A: " + e.getMessage());
        }
        try {
            countCubesByColor(cubes);
        } catch (IOException e) {
            System.err.println("error when writing to file B: " + e.getMessage());
        }
    }
}
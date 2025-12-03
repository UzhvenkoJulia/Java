// Клас для запуску тестування з вибором режиму (консоль/файл).

// Файл: test/TestRunner.java

/**
 * Клас TestRunner для демонстрації роботи класу Histogram
 * з вибором режиму введення (консоль або файл) та виводом результату.
 * Розробник: Ужвенко Юлія
 * Група: Комп'ютерна математика 1
 * Курс: 3
 * Дата: 30.11.2025
 * Час: 19:29
 */

package knu.mmf.histogram;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TestRunner {
    private static final String OUTPUT_FILE = "test_results.txt";
    private static final String TEXT_FILE_PATH = "resources/test_data.txt";
    private static final String BINARY_FILE_PATH = "resources/NZ_test.dat";

    public static void main(String[] args) {
        System.out.println("--- Running the Histogram Test Script ---");
        Scanner scanner = new Scanner(System.in);
        int mode = -1;

        while (mode < 1 || mode > 3) {
            System.out.println("Select a testing mode:");
            System.out.println("1 - Input from the console (manual addition)");
            System.out.println("2 - Input from a text file (" + TEXT_FILE_PATH + ")");
            System.out.println("3 - Input from a binary file (" + BINARY_FILE_PATH + ")");
            System.out.print("Your choice (1-3): ");

            try {
                mode = scanner.nextInt();
                if (mode < 1 || mode > 3) {
                    System.out.println("Incorrect selection. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Очищення буфера
            }
        }

        Histogram histogram = new Histogram(0.0, 10.0, 5); // Початкові параметри

        try {
            switch (mode) {
                case 1:
                    runConsoleTest(histogram, scanner);
                    break;
                case 2:
                    runFileTest(histogram, TEXT_FILE_PATH, false);
                    break;
                case 3:
                    runFileTest(histogram, BINARY_FILE_PATH, true);
                    break;
            }

            // Демонстрація роботи програми
            System.out.println("\n--- Demonstration of the Program's Work (Console) ---");
            histogram.display();
            histogram.displayAnalysis();

            // Виведення результату у файл
            System.out.println("\n--- Program report ---");
            histogram.writeToFile_Text(OUTPUT_FILE);
            System.out.println("The results of the program are output to the file: " + OUTPUT_FILE);

        } catch (Exception e) {
            System.err.println("Error while running the test: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void runConsoleTest(Histogram hist, Scanner scanner) {
        System.out.println("\n--- Mode: Console Input ---");
        System.out.println("Enter 5 numbers to add (using Option 2: To extremes):");
        for (int i = 0; i < 5; i++) {
            System.out.printf("Enter a number #%d: ", i + 1);
            try {
                double num = scanner.nextDouble();
                hist.addNumber_ToExtremes(num);
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input. Skip.");
                scanner.next();
            }
        }
    }

    private static void runFileTest(Histogram hist, String filePath, boolean isBinary) throws IOException {
        System.out.println("\n--- Mode: Input from File (" + (isBinary ? "Binary" : "Textual") + ") ---");
        // Демонстрація Варіанта 1: ігнорування чисел поза діапазоном [0, 10]
        if (isBinary) {
            hist.readFromFile_Binary(filePath);
        } else {
            hist.readFromFile_Text(filePath);
        }
        System.out.println("Data added successfully (Option 1: Ignore was used)");
    }

    /**
     * Цей метод демонструє повну функціональність та є аналогом JUnit-тестів.
     * Його можна викликати окремо, або інтегрувати в повноцінний JUnit-тест.
     */
    public static void runAllFunctionTests() {
        System.out.println("\n--- Full Functional Testing (JUnit Model) ---");
        Histogram hist = new Histogram(10.0, 20.0, 4);

        // 1. Тест Варіанта 1 (Ігнорування)
        System.out.println("Test 1: Option 1 (Ignore)");
        hist.clearFrequency(); // Метод має бути доступним
        hist.addNumber_Ignore(12.5); // [10, 12.5) -> Bin 0
        hist.addNumber_Ignore(17.5); // [17.5, 20] -> Bin 3
        hist.addNumber_Ignore(5.0); // Ігнорується
        hist.addNumber_Ignore(25.0); // Ігнорується
        // Очікувані частоти: [1, 0, 0, 1]. Total: 2
        assert hist.num() == 2 : "Test 1 FAIL: Total count incorrect";
        assert hist.numHist(0) == 1 : "Test 1 FAIL: Bin 0 incorrect";

        // 2. Тест Варіанта 2 (До Extremes)
        System.out.println("Test 2: Option 2 (To Extremes)");
        hist.clearFrequency();
        hist.addNumber_ToExtremes(5.0); // До Bin 0
        hist.addNumber_ToExtremes(25.0); // До Bin 3
        hist.addNumber_ToExtremes(11.0); // До Bin 0
        // Очікувані частоти: [2, 0, 0, 1]. Total: 3
        assert hist.num() == 3 : "Test 2 FAIL: Total count incorrect";
        assert hist.numHist(0) == 2 : "Test 2 FAIL: Bin 0 incorrect";
        assert hist.numHist(3) == 1 : "Test 2 FAIL: Bin 3 incorrect";

        // 3. Тест Аналізу (Mean/Median/Dev/Var) - простий випадок
        System.out.println("Test 3: Analysis");
        hist.clearFrequency();
        hist.addNumber_Ignore(15.0);
        hist.addNumber_Ignore(15.0); // Всього 2 елементи в центральному стовпці
        // BinWidth = (20-10)/4 = 2.5. Stovpці: [10, 12.5), [12.5, 15), [15, 17.5),
        // [17.5, 20]
        // 15.0 попадає в [15, 17.5) -> Bin 2
        // Очікувані частоти: [0, 0, 2, 0]. Midpoint Bin 2 = 16.25
        double expectedMean = 16.25;
        assert Math.abs(hist.mean() - expectedMean) < 0.01 : "Test 3 FAIL: Mean incorrect";
        // Варіація: ((16.25 - 16.25)^2 * 2) / (2-1) = 0.0
        assert Math.abs(hist.variance() - 0.0) < 0.001 : "Test 3 FAIL: Variance incorrect";

        System.out.println("--- All functional tests passed successfully! 🤩🥳🥳🥳🥳🥳 ---");
    }
}
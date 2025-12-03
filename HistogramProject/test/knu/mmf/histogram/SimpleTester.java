/**
 * Відділ контролю якості. Перевірка "для себе".
 * Розробник: Ужвенко Юлія
 * Група: Комп'ютерна математика 1
 * Курс: 3
 * Дата: 30.11.2025
 * Час: 19:10 
 */

package knu.mmf.histogram;

import static java.lang.System.out;
import static java.lang.Math.abs;

/**
 * Клас для простого тестування функціоналу Histogram без використання JUnit.
 */

public class SimpleTester {

    private static final double DELTA = 0.0001; // Допустима похибка

    public static void main(String[] args) {
        out.println("--- RUNNING SIMPLE UNIT TESTS (without JUnit) ---");

        testAddNumberIgnore();
        testAddNumberToExtremes();
        testUpdateBounds();
        testStatisticalMethods();
        testGetters();

        out.println("--- ALL TESTS COMPLETED ---");
    }

    /**
     * Перевірка методу addNumber_Ignore (Варіант 1).
     */

    private static void testAddNumberIgnore() {
        out.println("[Test 1] addNumber_Ignore");
        Histogram hist = new Histogram(0.0, 10.0, 4); // [0, 10], M=4

        hist.addNumber_Ignore(1.0); // Bin 0
        hist.addNumber_Ignore(9.9); // Bin 3
        hist.addNumber_Ignore(15.0); // Поза межами (ігнорується)

        // Перевірка 1: Загальна кількість
        check(hist.num() == 2, "Total number (must be 2)", 2, hist.num());
        // Перевірка 2: Частоти стовпців
        check(hist.numHist(0) == 1, "Frequency Bin 0", 1, hist.numHist(0));
        check(hist.numHist(3) == 1, "Frequency Bin 3", 1, hist.numHist(3));
        out.println();
    }

    /**
     * Перевірка методу addNumber_ToExtremes (Варіант 2).
     * Числа за межами мають падати в крайні стовпці.
     */

    private static void testAddNumberToExtremes() {
        out.println("[Test 2] addNumber_ToExtremes");
        Histogram hist = new Histogram(0.0, 10.0, 5); // [0, 10], M=5. Ширина = 2.

        hist.addNumber_ToExtremes(5.0); // Всередині (Bin 2: [4, 6))
        hist.addNumber_ToExtremes(-100.0); // Дуже мале -> Bin 0
        hist.addNumber_ToExtremes(100.0); // Дуже велике -> Bin 4 (останній)

        check(hist.num() == 3, "Total number", 3, hist.num());
        check(hist.numHist(0) == 1, "Bin 0 (should catch -100)", 1, hist.numHist(0));
        check(hist.numHist(4) == 1, "Bin 4 (should catch 100)", 1, hist.numHist(4));
        out.println();
    }

    /**
     * Перевірка методу addNumber_UpdateBounds (Варіант 3).
     * Перевіряємо, чи змінюються межі та чи скидаються частоти.
     */

    private static void testUpdateBounds() {
        out.println("[Test 3] addNumber_UpdateBounds");
        Histogram hist = new Histogram(10.0, 20.0, 4);

        hist.addNumber_UpdateBounds(12.0);
        check(hist.num() == 1, "First 1 element", 1, hist.num());

        // Додаємо викид, який змінить MIN
        hist.addNumber_UpdateBounds(5.0);

        // Межі мають стати [5.0, 20.0]. Попередні дані скидаються.
        check(hist.getMinHist() == 5.0, "Min changed to 5.0", 5.0, hist.getMinHist());
        check(hist.num() == 1, "Number after reset (new number only)", 1, hist.num());
        check(hist.numHist(0) == 1, "New number (5.0) in Bin 0", 1, hist.numHist(0));
        out.println();
    }

    /**
     * Перевірка середнього, дисперсії та стандартного відхилення.
     * Дані: 1, 1, 8, 8 (Центри стовпців: 1.25, 8.75). Середнє має бути 5.0
     */

    private static void testStatisticalMethods() {
        out.println("[Test 4] Statistics");
        Histogram hist = new Histogram(0.0, 10.0, 4);
        hist.addNumber_Ignore(1.0); // Bin 0
        hist.addNumber_Ignore(1.0); // Bin 0
        hist.addNumber_Ignore(8.0); // Bin 3
        hist.addNumber_Ignore(8.0); // Bin 3

        check(abs(hist.mean() - 5.0) < DELTA, "Average (Mean)", 5.0, hist.mean());
        check(abs(hist.variance() - 18.75) < 0.0002, "Dispersion (Variance)", 18.75, hist.variance());
        check(abs(hist.dev() - 4.3301) < 0.0002, "Deviation rate (Dev)", 4.3301, hist.dev());
        out.println();
    }

    /**
     * Перевірка геттерів (отримання полів).
     */

    private static void testGetters() {
        out.println("[Test 5] Getters");
        Histogram hist = new Histogram(0.0, 50.0, 10);

        check(hist.getMinHist() == 0.0, "getMinHist", 0.0, hist.getMinHist());
        check(hist.getMaxHist() == 50.0, "getMaxHist", 50.0, hist.getMaxHist());
        check(hist.getM() == 10, "getM", 10, hist.getM());
        check(hist.getFrequency().length == 10, "The size of the frequency array", 10, hist.getFrequency().length);
        out.println();
    }

    /**
     * Хелпер-метод для перевірки результатів.
     */

    private static void check(boolean condition, String testName, Object expected, Object actual) {
        if (condition) {
            out.printf("✅ Test '%s' passed.\n", testName);
        } else {
            out.printf("❌ Test '%s' FAILED. Expected: %s, Received: %s\n", testName, expected, actual);
        }
    }
}
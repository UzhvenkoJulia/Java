// Точка входу для демонстрації можливостей (введення з консолі).

// Файл: src/Main.java

/**
 * Основний клас Main для демонстрації роботи Histogram,
 * зокрема методу addNumber_UpdateBounds.
 * Розробник: Ужвенко Юлія
 * Група: Комп'ютерної математики 1
 * Дата: 30.11.2025
 * Час: 18:52
 */

package knu.mmf.histogram;

/**
 * Клас Main для демонстрації Варіанту 3 (оновлення меж) та
 * початкового тестування функціоналу Histogram.
 */

public class Main {

    /** Приватний конструктор, щоб запобігти створенню екземплярів класу-утиліти. */
    private Main() {
    }

    /**
     * Головний метод програми, точка входу.
     * Тут демонструється робота методу addNumber_UpdateBounds.
     * 
     * @param args Аргументи командного рядка (не використовуються).
     */

    public static void main(String[] args) {
        System.out.println("--- Main Demo (Option 3: Update Borders) ---");

        // Початкові параметри: [10, 20], M=4
        Histogram hist = new Histogram(10.0, 20.0, 4);
        hist.display();

        System.out.println("\nAdd the number 12.0 (inside)...");
        hist.addNumber_UpdateBounds(12.0);
        hist.display(); // Частоти: [1, 0, 0, 0]

        System.out.println("\nAdd the number 5.0 (out of bounds: changes min_hist)...");
        hist.addNumber_UpdateBounds(5.0);
        // Межі зміняться на [5.0, 20.0]. Частоти скинуться. 5.0 додасться.
        // Нові стовпці: (20-5)/4 = 3.75. [5, 8.75), [8.75, 12.5), [12.5, 16.25),
        // [16.25, 20]
        hist.display(); // Частоти: [1, 0, 0, 0] (12.0 було скинуто)

        System.out.println("\nAdd the number 25.0 (out of bounds: changes max_hist)...");
        hist.addNumber_UpdateBounds(25.0);
        // Межі зміняться на [5.0, 25.0]. Частоти скинуться. 25.0 додасться.
        // Нові стовпці: (25-5)/4 = 5.0. [5, 10), [10, 15), [15, 20), [20, 25]
        hist.display(); // Частоти: [0, 0, 0, 1] (5.0 було скинуто)

        System.out.println("\n--- Updated Histogram Analysis ---");
        hist.displayAnalysis();
    }
}
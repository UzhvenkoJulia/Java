// Файл: src/knu/mmf/histogram/DataGenerator.java

/**
 * Клас-утиліта для генерації тестового бінарного файлу NZ_test.dat.
 * Розробник: Ужвенко Юлія
 * Група: Комп'ютерна математика 1
 * Курс: 3
 * Дата: 30.11.2025
 * Час: 19:08 
 */

package knu.mmf.histogram;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class DataGenerator {
    private static final String BINARY_FILE_PATH = "resources/NZ_test.dat";

    /**
     * Генерує бінарний файл, записуючи послідовність чисел типу double.
     * 
     * @throws IOException якщо виникла помилка запису файлу.
     */

    public static void generateBinaryFile() throws IOException {
        // Створення директорії resources, якщо вона не існує
        File resourcesDir = new File("resources");
        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs();
        }

        System.out.println("Binary file generation: " + BINARY_FILE_PATH);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(BINARY_FILE_PATH))) {
            // Вихідні дані, які будуть додані в гістограму [0, 10]
            dos.writeDouble(0.5); // В діапазоні
            dos.writeDouble(4.5); // В діапазоні
            dos.writeDouble(9.5); // В діапазоні
            dos.writeDouble(1.0); // В діапазоні

            // Дані, які будуть ігноруватися TestRunner (тому що він використовує Варіант1)
            dos.writeDouble(-5.0); // Поза межами
            dos.writeDouble(12.0); // Поза межами

            System.out.println("The NZ_test.dat file has been successfully generated");
        }
    }

    // Додай(те) цей main-метод, щоб запустити генерацію окремо!
    public static void main(String[] args) {
        try {
            generateBinaryFile();
        } catch (IOException e) {
            System.err.println("Error generating binary file: " + e.getMessage());
        }
    }
}
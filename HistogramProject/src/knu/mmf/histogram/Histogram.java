// Основний клас для зберігання та аналізу гістограми. 
// Цей клас є серцем проекту. Він містить структуру даних та всі методи для модифікації, аналізу та роботи з файлами.

// Файл: src/Histogram.java

/**
 * Клас Histogram для зберігання та статистичної обробки даних у вигляді гістограми частот.
 * Розробник: Ужвенко Юлія
 * Група: Комп'ютерна математика 1
 * Курс: 3
 * Дата: 30.11.2025
 * Час: 18:10 
 */

package knu.mmf.histogram;

import java.io.*;
import java.util.Arrays;
import java.util.InputMismatchException; // конкретний тип помилки (винятку)
import java.util.Scanner;

/**
 * <p>
 * Представляє структуру даних Гістограма з фіксованою довжиною (M стовпців) на
 * інтервалі [min_hist, max_hist].
 * </p>
 *
 * <p>
 * Клас підтримує різні стратегії додавання чисел, які виходять за межі
 * інтервалу.
 * </p>
 */

public class Histogram {
    /** Мінімальне можливе значення в структурі. */
    private double minHist;
    /** Максимальне можливе значення в структурі. */
    private double maxHist;
    /** Кількість стовпців в гістограмі. */
    private int M;
    /** Частота - к-сть елементів в кожному стовпці гістограми. */
    private int[] frequency;
    /** Загальна кількість доданих елементів (сума всіх частот). */
    private int totalCount;

    /**
     * Конструктор для створення гістограми з початковими параметрами.
     * 
     * @param minHist Мінімальне можливе значення.
     * @param maxHist Максимальне можливе значення.
     * @param M       К-сть стовпців.
     * @throws IllegalArgumentException якщо minHist **&ge;** maxHist або M **&le;**
     *                                  0.
     */

    public Histogram(double minHist, double maxHist, int M) {
        if (minHist >= maxHist || M <= 0) {
            throw new IllegalArgumentException("Incorrect parameters: min < max and M > 0");
        }
        this.minHist = minHist;
        this.maxHist = maxHist;
        setM(M); // Ініціалізація M та frequency
    }

    // --- Методи-модифікатори ---

    /**
     * Встановити максимальне можливе значення в структурі.
     * Якщо нове значення не містить поточний інтервал, скидає frequency.
     * 
     * @param m Нове максимальне значення.
     * @throws IllegalArgumentException m **&le;** minHist.
     */

    public void setMax(double m) {
        if (m <= minHist) {
            throw new IllegalArgumentException("The maximum value must be greater than the minimum");
        }
        this.maxHist = m;
        // Потрібно перерахувати частоти, якщо діапазон змінився, або скинути їх
        // У цьому варіанті просто скидаємо, оскільки перерахунок складний
        clearFrequency();
    }

    /**
     * Встановити мінімальне можливе значення в структурі.
     * Якщо нове значення не містить поточний інтервал, скидає frequency.
     * 
     * @param m Нове мінімальне значення.
     * @throws IllegalArgumentException якщо m >= maxHist.
     */

    public void setMin(double m) {
        if (m >= maxHist) {
            throw new IllegalArgumentException("The minimum value must be less than the maximum");
        }
        this.minHist = m;
        clearFrequency();
    }

    /**
     * Встановити кількість стовпців на інтервалі [min_hist, max_hist].
     * 
     * @param m Нова кількість стовпців.
     * @throws IllegalArgumentException m **&le;** 0.
     */

    public void setM(int m) {
        if (m <= 0) {
            throw new IllegalArgumentException("The number of columns M must be > 0");
        }
        this.M = m;
        this.frequency = new int[M];
        this.totalCount = 0;
    }

    /** Скидає частоти та загальну кількість елементів. */
    public void clearFrequency() {
        if (frequency != null) {
            Arrays.fill(frequency, 0);
            totalCount = 0;
        }
    }

    // --- Допоміжні методи для додавання чисел ---

    /**
     * Обчислює індекс стовпця для заданого числа.
     * 
     * @param x Число для додавання.
     * @return Індекс стовпця (від 0 до M-1).
     */

    private int getBinIndex(double x) {
        if (x < minHist)
            return -1; // Менше мінімуму
        if (x > maxHist)
            return M; // Більше або дорівнює максимуму

        // Довжина інтервалу
        double range = maxHist - minHist;
        // Ширина одного стовпця
        double binWidth = range / M;
        // Індекс стовпця
        int index = (int) Math.floor((x - minHist) / binWidth);

        // Забезпечення, що останнє число в діапазоні (якщо воно менше maxHist)
        // потрапляє у відповідний bin
        if (index == M) {
            index--;
        }
        return index;
    }

    // --- Методи додавання чисел (3 варіанти) ---

    /**
     * Варіант 1: Додати дійсне число. Числа, що не входять в інтервал, ігноруються.
     * 
     * @param x Число для додавання.
     */

    public void addNumber_Ignore(double x) {
        int index = getBinIndex(x);
        if (index >= 0 && index < M) {
            frequency[index]++;
            totalCount++;
        }
    }

    /**
     * Варіант 2: Додати дійсне число. Числа, що не входять в інтервал, додаються до
     * крайніх стовпців.
     * 
     * @param x Число для додавання.
     */

    public void addNumber_ToExtremes(double x) {
        int index = getBinIndex(x);
        if (index < 0) { // Менше minHist
            frequency[0]++;
        } else if (index >= M) { // Більше або дорівнює maxHist
            frequency[M - 1]++;
        } else { // У діапазоні
            frequency[index]++;
        }
        totalCount++;
    }

    /**
     * Варіант 3: Додати дійсне число. Числа, що не входять в інтервал, змінюють
     * minHist, maxHist.
     * Це вимагає перерахунку всієї гістограми, оскільки змінюється ширина стовпця.
     * Для простоти реалізації, ми просто оновлюємо межі та скидаємо частоти.
     * Повна коректна реалізація вимагає збереження всіх даних, що суперечить умові.
     *
     * УВАГА: Реалізація скидає існуючі частоти!
     * 
     * @param x Число для додавання.
     */

    public void addNumber_UpdateBounds(double x) {
        boolean boundsChanged = false;
        if (x < minHist) {
            minHist = x;
            boundsChanged = true;
        }
        if (x > maxHist) {
            maxHist = x;
            boundsChanged = true;
        }

        // Якщо межі змінилися, існуюча гістограма стає недійсною.
        if (boundsChanged) {
            System.out.println("Histogram boundaries changed! Frequencies reset");
            clearFrequency();
            // Потрібно знову додати це число, оскільки старий індекс недійсний
            // Тут використовуємо Варіант1 для нового додавання
            int index = getBinIndex(x);
            if (index >= 0 && index < M) {
                frequency[index]++;
                totalCount++;
            }
        } else {
            int index = getBinIndex(x);
            if (index >= 0 && index < M) {
                frequency[index]++;
                totalCount++;
            }
        }
    }

    /**
     * Додати масив чисел в структуру, використовуючи Варіант1 (ігнорування).
     * 
     * @param data     Масив чисел.
     * @param dataSize Розмір масиву.
     */

    public void addBatch(double[] data, int dataSize) {
        for (int i = 0; i < dataSize; i++) {
            addNumber_Ignore(data[i]);
        }
    }

    // --- Методи для введення/виведення з файлів ---

    /**
     * Читання масиву чисел з текстового файлу та додавання їх (Варіант1:
     * ігнорування).
     * Кожне число має бути на новому рядку або розділене пробілом/комою.
     * 
     * @param filePath Шлях до текстового файлу.
     * @throws IOException якщо виникла помилка читання файлу.
     */

    public void readFromFile_Text(String filePath) throws IOException {
        System.out.println("Reading from a text file: " + filePath);
        try (Scanner scanner = new Scanner(new File(filePath))) {
            scanner.useLocale(java.util.Locale.US);
            while (scanner.hasNextDouble()) {
                addNumber_Ignore(scanner.nextDouble());
            }
        }
    }

    /**
     * Читання масиву чисел з бінарного файлу та додавання їх (Варіант1:
     * ігнорування).
     * Очікується, що числа записані як послідовність 'double'.
     * 
     * @param filePath Шлях до бінарного файлу.
     * @throws IOException якщо виникла помилка читання файлу.
     */

    public void readFromFile_Binary(String filePath) throws IOException {
        System.out.println("Reading from a binary file: " + filePath);
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filePath))) {
            while (true) {
                try {
                    double x = dis.readDouble();
                    addNumber_Ignore(x);
                } catch (EOFException e) {
                    break; // Кінець файлу
                }
            }
        }
    }

    /**
     * Читає одне дійсне число з консолі та додає його до гістограми
     * (використовує Варіант1: Ігнорування, у разі помилки вводу ігнорує).
     * 
     * @param scanner Об'єкт Scanner для читання вводу.
     */

    public void readNumberFromConsole(Scanner scanner) {
        System.out.print("Enter a number: ");
        try {
            double x = scanner.nextDouble();
            addNumber_Ignore(x);
        } catch (InputMismatchException e) {
            // Тепер імпорт використовується!
            System.err.println("Error: Incorrect number format");
            scanner.next(); // Очищення некоректного вводу
        }
    }

    /**
     * Збереження поточної гістограми (параметрів та частот) у текстовий файл.
     * 
     * @param filePath Шлях для запису.
     * @throws IOException якщо виникла помилка запису файлу.
     */

    public void writeToFile_Text(String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            pw.println("minHist: " + minHist);
            pw.println("maxHist: " + maxHist);
            pw.println("M: " + M);
            pw.println("totalCount: " + totalCount);
            pw.println("Frequencies:");
            for (int i = 0; i < M; i++) {
                pw.println("Bin " + i + ": " + frequency[i]);
            }
        }
    }

    // --- Аналіз гістограми ---

    /**
     * К-сть елементів в гістограмі.
     * 
     * @return Загальна к-сть елементів.
     */

    public int num() {
        return totalCount;
    }

    /**
     * Частота відповідного стовпчика.
     * 
     * @param i Індекс стовпчика (від 0 до M-1).
     * @return Частота стовпчика.
     * @throws IndexOutOfBoundsException якщо індекс виходить за межі [0, M-1].
     */

    public int numHist(int i) {
        if (i < 0 || i >= M) {
            throw new IndexOutOfBoundsException("Column index must be in the range [0, " + (M - 1) + "]");
        }
        return frequency[i];
    }

    /**
     * Обчислює середнє значення гістограми.
     * Використовує середини стовпців як репрезентативні значення.
     * 
     * @return Середнє значення.
     */

    public double mean() {
        if (totalCount == 0)
            return 0.0;
        double sum = 0.0;
        double binWidth = (maxHist - minHist) / M;

        for (int i = 0; i < M; i++) {
            // Середина стовпця
            double midPoint = minHist + (i + 0.5) * binWidth;
            sum += midPoint * frequency[i];
        }
        return sum / totalCount;
    }

    /**
     * Обчислює медіану гістограми.
     * Використовує лінійну інтерполяцію в медіанному стовпці.
     * 
     * @return Медіана.
     */

    public double median() {
        if (totalCount == 0)
            return 0.0;
        double binWidth = (maxHist - minHist) / M; // ширина одного інтервалу (стовпчика)
        double medianPosition = totalCount / 2.0;

        int currentCount = 0;
        for (int i = 0; i < M; i++) {
            int nextCount = currentCount + frequency[i];

            if (nextCount >= medianPosition) {
                // Знайдено медіанний стовпчик
                double L = minHist + i * binWidth; // Нижня межа медіанного стовпця
                double F_prev = currentCount; // Накопичена частота до медіанного стовпця
                double f_m = frequency[i]; // Частота медіанного стовпця

                /*
                 * L (Lower limit) — Нижня межа інтервалу, в якому знаходиться медіана.
                 * N — Загальна кількість елементів (сума всіх частот).
                 * N/2 — Пошук середини.
                 * F_prev — Накопичена частота до медіанного інтервалу. Це сума
                 * всього, що знаходяться в стовпчиках лівіше від того, де я шукаю
                 * медіану.
                 * f_m — Частота самого медіанного інтервалу (скільки чогось в цьому
                 * конкретному стовпчику).
                 * h — Ширина інтервалу (те саме, що binWidth).
                 */

                if (f_m == 0) {
                    // Якщо частота 0, то медіана на межі, або 0, залежно від розподілу
                    // Для простоти, повертаємо середину поточного стовпця, якщо це не перша межа
                    return L + binWidth / 2.0;
                }

                // Формула для медіани згрупованих даних: L + ((N/2 - F_prev) / f_m) * h
                return L + ((medianPosition - F_prev) / f_m) * binWidth;
            }
            currentCount = nextCount;
        }
        // Забезпечення на випадок крайніх значень
        return maxHist;
    }

    /**
     * Обчислює дисперсію гістограми.
     * 
     * @return Дисперсія.
     */

    public double variance() {
        if (totalCount <= 1)
            return 0.0;

        double mean = mean();
        double sumSqDiff = 0.0;
        double binWidth = (maxHist - minHist) / M;

        for (int i = 0; i < M; i++) {
            double midPoint = minHist + (i + 0.5) * binWidth;
            double diff = midPoint - mean;
            sumSqDiff += frequency[i] * diff * diff;
        }

        // Дисперсія вибірки (з n-1 у знаменнику)
        return sumSqDiff / (totalCount - 1);
    }

    /**
     * Обчислює середнє відхилення (стандартне відхилення).
     * 
     * @return Середнє відхилення.
     */

    public double dev() {
        return Math.sqrt(variance());
    }

    // --- Додаткові характеристики (на вибір) ---

    /**
     * Обчислює асиметрію (Skewness) гістограми (коефіцієнт Пірсона 2).
     * 
     * Коефіцієнт асиметрії Пірсона №2 (Pearson's Second Coefficient of Skewness) —
     * це простий спосіб визначити у цифрах, наскільки моя гістограма "перекошена"
     * вліво або вправо, використовуючи Медіану.
     * 
     * У статистиці є емпіричне правило для трохи перекошених розподілів: різниця
     * між середнім і модою
     * приблизно в три рази більша, ніж різниця між середнім і медіаною. Множник 3
     * дозволяє зробити цей коефіцієнт схожим на перший, але без використання Моди.
     * 
     * @return Асиметрія.
     */

    public double skewness() {
        if (totalCount <= 1)
            return 0.0;
        double mean = mean();
        double dev = dev();
        if (dev == 0.0)
            return 0.0; // Уникнення ділення на нуль

        // Обчислення третього центрального моменту
        double sumCubedDiff = 0.0;
        double binWidth = (maxHist - minHist) / M;

        for (int i = 0; i < M; i++) {
            double midPoint = minHist + (i + 0.5) * binWidth;
            double diff = midPoint - mean;
            sumCubedDiff += frequency[i] * Math.pow(diff, 3);
        }

        double moment3 = sumCubedDiff / totalCount;
        return moment3 / Math.pow(dev, 3);
    }

    /**
     * Обчислює ексцес (Kurtosis) гістограми.
     * 
     * @return Ексцес (надлишковий ексцес) - описує "гостроверхість" графіка та
     *         товщину його "хвостів" (країв).
     */

    public double kurtosis() {
        if (totalCount <= 1)
            return 0.0;
        double mean = mean();
        double dev = dev();
        if (dev == 0.0)
            return 0.0;

        // Обчислення четвертого центрального моменту
        double sumFourthDiff = 0.0;
        double binWidth = (maxHist - minHist) / M;

        for (int i = 0; i < M; i++) {
            double midPoint = minHist + (i + 0.5) * binWidth;
            double diff = midPoint - mean;
            sumFourthDiff += frequency[i] * Math.pow(diff, 4);
        }

        double moment4 = sumFourthDiff / totalCount;
        // Надлишковий ексцес (excess kurtosis) = (M4 / dev^4) - 3
        return (moment4 / Math.pow(dev, 4)) - 3.0;
    }

    /**
     * Обчислює моду (Mode) гістограми.
     * Повертає середину стовпця з максимальною частотою.
     * 
     * @return Мода.
     */

    public double mode() {
        if (totalCount == 0)
            return 0.0;
        int maxFreq = 0;
        int modeIndex = 0;

        for (int i = 0; i < M; i++) {
            if (frequency[i] > maxFreq) {
                maxFreq = frequency[i];
                modeIndex = i;
            }
        }
        double binWidth = (maxHist - minHist) / M;
        return minHist + (modeIndex + 0.5) * binWidth;
    }

    /**
     * Обчислює діапазон (Range) гістограми.
     * 
     * @return Діапазон.
     */

    public double range() {
        return maxHist - minHist;
    }

    /**
     * Обчислює середнє абсолютне відхилення (Mean Absolute Deviation - MAD).
     * 
     * @return Середнє абсолютне відхилення.
     */

    public double meanAbsoluteDeviation() {
        if (totalCount == 0)
            return 0.0;
        double mean = mean();
        double sumAbsDiff = 0.0;
        double binWidth = (maxHist - minHist) / M;

        for (int i = 0; i < M; i++) {
            double midPoint = minHist + (i + 0.5) * binWidth;
            double diff = Math.abs(midPoint - mean);
            sumAbsDiff += frequency[i] * diff;
        }

        return sumAbsDiff / totalCount;
    }

    /**
     * Обчислює коефіцієнт варіації (Coefficient of Variation - CV).
     * 
     * @return Коефіцієнт варіації (у відсотках).
     */

    public double coefficientOfVariation() {
        double mean = mean();
        double dev = dev();
        if (mean == 0.0)
            return 0.0;
        return (dev / mean) * 100.0; // У відсотках
    }

    /**
     * Обчислює перший квартиль (Q1) гістограми.
     * Використовує лінійну інтерполяцію.
     * 
     * @return Перший квартиль.
     */

    public double quartile1() {
        if (totalCount == 0)
            return 0.0;
        double binWidth = (maxHist - minHist) / M;
        double q1Position = totalCount / 4.0;

        int currentCount = 0;
        for (int i = 0; i < M; i++) {
            int nextCount = currentCount + frequency[i];

            if (nextCount >= q1Position) {
                // Знайдено квартильний стовпчик
                double L = minHist + i * binWidth; // Нижня межа
                double F_prev = currentCount; // Накопичена частота до
                double f_q = frequency[i]; // Частота квартильного стовпчика

                if (f_q == 0) {
                    return L; // Повертаємо межу, якщо частота нуль
                }

                // Формула: L + ((N/4 - F_prev) / f_q) * h
                return L + ((q1Position - F_prev) / f_q) * binWidth;
            }
            currentCount = nextCount;
        }
        return maxHist;
    }

    // --- Методи введення/виведення (для консолі) ---

    /**
     * Вивід параметрів та частот гістограми на консоль.
     */

    public void display() {
        System.out.println("--- Histogram Options ---");
        System.out.printf("Range: [%.2f, %.2f]\n", minHist, maxHist);
        System.out.println("Number of columns (M): " + M);
        System.out.println("Total number of items: " + totalCount);
        System.out.println("Column width: " + (maxHist - minHist) / M);
        System.out.println("--- Frequencies ---");

        double binWidth = (maxHist - minHist) / M;
        for (int i = 0; i < M; i++) {
            double start = minHist + i * binWidth;
            double end = minHist + (i + 1) * binWidth;
            String interval = String.format("[%.2f, %.2f%s", start, end, (i == M - 1) ? "]" : ")");
            System.out.printf("Column %2d %s: %d\n", i, interval, frequency[i]);
        }
    }

    /**
     * Вивід усіх характеристик аналізу на консоль.
     */

    public void displayAnalysis() {
        System.out.println("\n--- Histogram Analysis ---");
        System.out.printf("Number of elements (N): %d\n", num());
        System.out.printf("Average value(Mean): %.4f\n", mean());
        System.out.printf("Median: %.4f\n", median());
        System.out.printf("Mode - center of the highest column: %.4f\n", mode());
        System.out.printf("Dispersion (Variance): %.4f\n", variance());
        System.out.printf("Average deviation (Std Dev): %.4f\n", dev());
        System.out.printf("Range: %.4f\n", range());
        System.out.printf("Coefficient of variation (CV): %.4f%%\n", coefficientOfVariation());
        System.out.printf("Asymmetry (Skewness): %.4f\n", skewness());
        System.out.printf("Excess (Kurtosis): %.4f\n", kurtosis());
        System.out.printf("Average abs. deviation (MAD): %.4f\n", meanAbsoluteDeviation());
        System.out.printf("First quartile (Q1): %.4f\n", quartile1());
    }

    // --- Getters для полів (за потребою) ---

    /**
     * Повертає мінімальне можливе значення інтервалу гістограми.
     * 
     * @return Мінімальна межа (minHist).
     */

    public double getMinHist() {
        return minHist;
    }

    /**
     * Повертає максимальне можливе значення інтервалу гістограми.
     * 
     * @return Максимальна межа (maxHist).
     */

    public double getMaxHist() {
        return maxHist;
    }

    /**
     * Повертає кількість стовпців гістограми.
     * 
     * @return Кількість стовпців (M).
     */

    public int getM() {
        return M;
    }

    /**
     * Повертає масив частот для кожного стовпця гістограми.
     * 
     * @return Масив frequency.
     */

    public int[] getFrequency() {
        return frequency;
    }
}
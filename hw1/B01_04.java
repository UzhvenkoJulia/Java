// Ввести (через консоль) три цілих числа та обчислити значення їх середнього геометричного. 
// Результат вивести з точністю до чотирьох знаків після коми.

/*клас Scanner для читання даних з консолі
обчислення середнього геометричного ∛(a * b * c) - метод Math.pow()
форматування результату до чотирьох знаків після коми - System.out.printf()*/

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class B01_04 {
    public static void main(String[] args) {
        // Locale.US використовується, щоб роздільником для дробових чисел гарантовано
        // була крапка
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);

        try {

            System.out.print("integer (a): ");
            int a = scanner.nextInt();
            System.out.print("(b): ");
            int b = scanner.nextInt();
            System.out.print("(c): ");
            int c = scanner.nextInt();

            // добуток чисел - 'long' про всяк випадок, щоб уникнути переповнення, якщо
            // числа будуть великими
            long product = (long) a * b * c;

            // Середнє геометричне для трьох чисел - це кубічний корінь з їх добутку
            // Важливо писати 1.0/3.0, щоб отримати дробовий результат (0.333...), оскільки
            // цілочисельне ділення 1/3 дало б 0
            double geometricMean = Math.pow(product, 1.0 / 3.0);

            // System.out.printf() дозволяє форматувати вивід
            System.out.printf("The geometric mean of the numbers %d, %d, %d is: %.4f%n", a, b, c, geometricMean);

        } catch (InputMismatchException e) {
            // користувач вводить не ціле число
            System.out.println("Error");
        } finally {
            scanner.close();
        }
    }
}
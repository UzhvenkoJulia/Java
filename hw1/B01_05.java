/* Ввести (через консоль) два додатних цілих числа N та M. 
Вивести N випадкових цілих чисел у діапазоні від 0 до M. 
Кожне число виводити в окремому рядку. 
Опрацювати випадок, коли числа N та M вводяться як аргументи командного рядка.

Примітка. 
Для отримання випадкового цілого числа, використати формулу
(int) (Math.random() * M); */

// здатність працювати у двох режимах
// [0, M-1]

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class B01_05 {
    public static void main(String[] args) {
        int n = 0;
        int m = 0;

        if (args.length >= 2) {
            System.out.println("Operation mode: obtaining data from command line arguments");
            try {
                // (рядок) на число
                n = Integer.parseInt(args[0]);
                m = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Error");
                return;
            }
        } else {
            System.out.println("interactive input from the console");
            Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
            try {
                System.out.print("number of random numbers (N): ");
                n = scanner.nextInt();
                System.out.print("upper limit of the range (M): ");
                m = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error");
                scanner.close();
                return;
            }
            scanner.close();
        }

        if (n <= 0 || m <= 0) {
            System.out.println("N and M must be positive integers");
            return;
        }

        System.out.println("--- Result ---");
        System.out.println("We generate " + n + " random numbers in the range from 0 to " + (m - 1) + ":");

        // цикл, що повторюється N разів
        for (int i = 0; i < n; i++) {
            // Math.random() повертає дробове число від 0.0 до 1.0 (не включаючи 1.0)
            // * його на M, щоб отримати число від 0.0 до M (не включаючи M)
            // int, без дробової частини
            int randomNumber = (int) (Math.random() * m);

            System.out.println(randomNumber);
        }
    }
}
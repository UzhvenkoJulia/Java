// Базові конструкції
import java.util.Scanner;


public class B0206 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("number of elements in the array: ");

        int n = scanner.nextInt();
        double[] array = new double[n];
        double sumOfReciprocals = 0.0; // сума обернених значень (1/x)

        for (int i = 0; i < n; i++) { // з i=0
            System.out.print("elem " + (i + 1) + ": ");
            array[i] = scanner.nextDouble();
            if (array[i] == 0) { // 1/0 -> помилка
                System.out.println("the array must not contain 0");
                return;
            }
            sumOfReciprocals += 1.0 / array[i];
        }

        if (n == 0) {
            System.out.println("empty");
            return;
        }
        double harmonicMean = n / sumOfReciprocals;
        System.out.println("harmonic mean value: " + harmonicMean);
        scanner.close();
    }
}
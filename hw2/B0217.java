import java.util.Scanner;


// h
public class B0217 {
    public static double calculateArctanSum(double x, double epsilon) {

        // умова збіжності ряду: |x| < 1
        if (Math.abs(x) >= 1) {
            System.out.println("Error");
            return Double.NaN;
        }

        double sum = 0.0;
        double term = x;  
        int k = 0;      
        int sign = 1;  // зміна знаку доданка (+1, -1, +1, ...)

        while (Math.abs(term) > epsilon) {
            sum += term;
            k++;
            /*
            term = (sign * x^(2k+1)) / (2k+1)
            попередній доданок - уникаємо повторних обчислень степенів
            далі = (попередній терм) * (-1) * x^2 * (2k-1)/(2k+1)
            pow() для нових значень, наступних
            */
            term = sign * Math.pow(x, 2 * k + 1) / (2 * k + 1);
            sign *= -1; // для наступного доданка
        }
        return sum;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("The value of x (from -1 to 1): ");
        double x = scanner.nextDouble();
        System.out.print("Accuracy epsilon (ε > 0): ");
        double epsilon = scanner.nextDouble();
        double result = calculateArctanSum(x, epsilon);

        if (!Double.isNaN(result)) {
            System.out.println("The sum of the series for arctan(" + x + ") with precision " + epsilon + " is: " + result);
            System.out.println("The value of arctan(" + x + ") using Math.atan(): " + Math.atan(x));
        }
        scanner.close();
    }
}
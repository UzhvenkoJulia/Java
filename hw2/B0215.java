import java.util.Scanner;


public class B0215 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        final int BITS_IN_SHORT = 16;
        System.out.print("Enter a double-byte integer (from -32768 to 32767): ");
        short number = scanner.nextShort(); // short - це двобайтове ціле число в Java

        int zeroBitCount = 0;
        /*
        Integer.toBinaryString() конвертує число в рядок з його двійковим представленням
        & 0xFFFF використовується для коректного виведення
        & (AND) перевірка останнього біта
        (number & 1) порівнює наймолодший біт числа з бітом 1
        */
        System.out.println("Binary representation of a number: " + Integer.toBinaryString(number & 0xFFFF));
       
        for (int i = 0; i < BITS_IN_SHORT; i++) {
            if ((number & 1) == 0) {
                // результат 0 - біт 0, лічильник+
                zeroBitCount++;
            }
            // перевірка наступного біта на кожній ітерації циклу
            number = (short) (number >> 1); // 1101 >> 1 стає 0110
        }
        System.out.println("Number of zero bits: " + zeroBitCount);
        scanner.close();
    }
}
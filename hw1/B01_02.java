// Надрукувати всі аргументи з командного рядка у зворотному порядку.
// Запустити цю програму через командний рядок.

public class B01_02 {
    public static void main(String[] args) {

        System.out.println("Command line arguments in reverse order:");

        // чи були взагалі передані аргументи
        if (args.length == 0) {
            System.out.println("No arguments were provided.");
            return;
        }

        // 1. початок з індексу останнього елемента: args.length - 1
        // 2. продовж цикл, поки індекс 'i' більший або рівний 0
        // 3. зменш індекс на 1 на кожній ітерації (i--)

        for (int i = args.length - 1; i >= 0; i--) {
            System.out.println(args[i]);
        }
    }
}
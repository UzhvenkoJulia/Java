// Підрахувати добуток аргументів командного рядка. 
// Якщо аргументи не є цілими числами, вивести про це повідомлення.

/*Ключовим елементом у цьому завданні є обробка можливих помилок за допомогою конструкції try-catch. 
Це дозволяє програмі "спробувати" перетворити аргумент на число і, якщо це не вдасться, "зловити" помилку 
і виконати альтернативний код, не перериваючи роботу. */

public class B01_03 {
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please provide numerical arguments for calculating the product");
            return;
        }

        // для зберігання добутку
        // множення на 0 = 0
        // long, щоб уникнути переповнення, якщо добуток буде великим

        long product = 1;

        System.out.println("Calculating the product of arguments...");

        // цикл for-each, щоб пройтися по кожному аргументу
        for (String arg : args) {
            try {
                // СПРОБА: перетворити поточний аргумент (який є рядком String) на ціле число
                // int
                int number = Integer.parseInt(arg);

                // Якщо перетворення вдалося, * поточний добуток на це число
                product *= number;

            } catch (NumberFormatException e) {
                // ПЕРЕХОПЛЕННЯ ПОМИЛКИ: цей блок коду виконається, якщо Integer.parseInt(arg)
                // не зміг перетворити рядок на число
                // 'NumberFormatException' - це стандартний тип помилки для таких випадків
                System.out.println("Error: argument '" + arg + "' is not an integer and will be ignored");
            }
        }

        System.out.println("====================");
        System.out.println("Final product: " + product);
    }
}
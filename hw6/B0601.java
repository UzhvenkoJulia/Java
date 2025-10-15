import java.time.LocalDate;  // поточна дата
import java.time.format.DateTimeFormatter;
// регулярні вирази
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class B0601 {
    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatter);
        System.out.println("current date for replacement: " + formattedDate);
        System.out.println("---");

        String text = "consultation is scheduled for 15.10.2025. " +
                      "deadline: __.__.____. " +
                      "training start date: 08.04.2025. " +
                      "expiration date: __.__.____.";

        System.out.println("source text:");
        System.out.println(text);
        System.out.println("---");

        /* створення регулярного виразу (Regex) для пошуку
            \. - точка (потрібно екранувати, бо . має спеціальне значення в regex)
           підкреслення у форматі __.__.____: (_+\._+\._+)
            _+ - одне або більше підкреслень
            \. - точка (екранована)
           об'єднуємо обидва вирази за допомогою оператора "АБО" (|)  */
        String regex = "(\\d{2}\\.\\d{2}\\.\\d{4})|(_+\\._+\\._+)";

        Pattern pattern = Pattern.compile(regex);
        // пошук збігів у тексті
        Matcher matcher = pattern.matcher(text);
        // заміна знайдених збігів
        String result = matcher.replaceAll(formattedDate);

        System.out.println("after replacement:");
        System.out.println(result);
    }
}
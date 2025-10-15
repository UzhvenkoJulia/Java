import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class B0602 {
    public static void main(String[] args) {
        String text = "contacts: +38 (067) 963-45-62, " +
                      "095 876 54 32, " +
                      "(050)1112233, " +
                      "380990001122, " +
                      "office number: 044-567-61-74. " +
                      "no number: 123456789."; 
        System.out.println("---string parsing---");
        System.out.println(text);
        System.out.println("---");

        /*
         * \\+?                            - знак '+'
         * (\\s*\\(?\\d+\\)?\\s*[-.]?){2,} - код країни, міста, оператора
         * \\s*                            - 0 або більше пробілів
         * \\(?\\d+\\)?                    - дужки навколо 1+ цифр
         * [-.]?                           - тире (-) або крапка (.)
         * {2,}                            - повторення цієї структури 2 або більше разів
         * (\\d[\\s-.]?){2,}\\d            - завершальна частина номера
         * \\d[\\s-.]?                     - цифра, за якою опціонально слідує роздільник
         * {2,}\\d                         - повторюється 2+ рази і закінчується цифрою
         */
         
        String preciseRegex = "\\+?(\\s*\\(?\\d+\\)?\\s*[-.]?){2,}(\\d[\\s-.]?){2,}\\d";


        Pattern pattern = Pattern.compile(preciseRegex);
        Matcher matcher = pattern.matcher(text);

        System.out.println("phone numbers found:");
        int count = 0; 

        while (matcher.find()) {
            // matcher.group(0) повертає весь знайдений збіг (номер телефону)
            System.out.println("[" + ++count + "] " + matcher.group(0));
        }

        if (count == 0) {
            System.out.println("not found");
        }
    }
}
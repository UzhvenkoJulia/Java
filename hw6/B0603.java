import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class B0603 {
    private static final String ARITHMETIC_REGEX =
            "^[\\s]*[+-]?[\\s]*\\d+" +
            "([\\s]*[+\\-*/][\\s]*[+-]?[\\s]*\\d+)*" +
            "[\\s]*$";
    public static boolean isExpressionValid(String expression) {
        Pattern pattern = Pattern.compile(ARITHMETIC_REGEX);
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String[] testExpressions = {
            "+2 - 57*33 + 25 / - 4",  
            "123 + 45 / 6",           
            "-100 * +50",             
            " 1 + 2 * 3 - 4 / 5 ",    
            " 5 + * 6",                
            "7 / 8 + ",               
            "12 (3 + 4)",             
            "10 % 5",                 
            "   -  5   * 100  +  1  ", 
            ""                        
        };

        System.out.println("---analysis of the syntactic correctness of arithmetic expressions---");
        
        for (String expr : testExpressions) {
            
            boolean isValid = isExpressionValid(expr);
            
            System.out.printf("expression: \"%s\"\n", expr);
            System.out.printf("result: %s\n", isValid ? "✅" : "❌");
            System.out.println("---");
        }
    }
}
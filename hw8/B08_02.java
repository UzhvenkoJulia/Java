import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class B08_02 {

    public static boolean checkBrackets(String s) {
        Stack<Character> stack = new Stack<>();
        
        Map<Character, Character> bracketMap = new HashMap<>();
        bracketMap.put(')', '(');
        bracketMap.put(']', '[');
        bracketMap.put('}', '{');

        for (char ch : s.toCharArray()) {
            if (bracketMap.containsValue(ch)) {
                stack.push(ch);
            } else if (bracketMap.containsKey(ch)) {
                
                if (stack.isEmpty()) {
                    return false;
                }
                
                char lastOpen = stack.pop();
                if (lastOpen != bracketMap.get(ch)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        String test1 = "([]{})";
        String test2 = "([)]";
        String test3 = "{[}";
        String test4 = "{[()]}";
        String test5 = "(()";

        System.out.println(test1 + ": " + (checkBrackets(test1) ? "+" : "-")); // +
        System.out.println(test2 + ": " + (checkBrackets(test2) ? "+" : "-")); // -
        System.out.println(test3 + ": " + (checkBrackets(test3) ? "+" : "-")); // -
        System.out.println(test4 + ": " + (checkBrackets(test4) ? "+" : "-")); // +
        System.out.println(test5 + ": " + (checkBrackets(test5) ? "+" : "-")); // -
    }
}
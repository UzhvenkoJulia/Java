public class B0501 {

    public static String filter(String s) throws IllegalArgumentException {
        StringBuilder sb = new StringBuilder();

        int b = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '(') {
                if (b == 0) {
                    b = 1;
                } else {
                    throw new IllegalArgumentException("have nested parentheses String: " + s + " at position " + i);
                }
            } else if (c == ')') {
                if (b == 1) {
                    b = 0;
                } else {
                    throw new IllegalArgumentException("incorrect placement of parentheses (extra closing parenthesis) String: " + s + " at position " + i);
                }
            } else {
                if (b == 0) {
                    sb.append(c);
                }
            }
        }

        if (b != 0) {
            throw new IllegalArgumentException("the closing parenthesis is missing String: " + s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] t = {
            "(text in brackets) example",
            "Hello (Deleted) World!",
            "no brackets",
            "(Start) and (End)",
            "abc(def)ghi",
            "а(б)в(г)д",
            "only (this) will remain"
        };
        String[] e = {
            "one (time",
            "two ) times",
            "three ( (nested) )",
            "()",
            "()("
        };

        System.out.println("---");

        for (String s : t) {
            try {
                String result = filter(s);
                System.out.println("Exit:  \"" + s + "\"");
                System.out.println("Entrance: \"" + result + "\"");
                System.out.println("---");
            } catch (IllegalArgumentException err) {
                System.out.println("Exit:  \"" + s + "\"");
                System.out.println("ERROR: " + err.getMessage());
                System.out.println("---");
            }
        }

        System.out.println("\n-with errors-");
        for (String s : e) {
            try {
                String result = filter(s);
                System.out.println("Exit:  \"" + s + "\"");
                System.out.println("Entrance: \"" + result + "\"");
                System.out.println("---");
            } catch (IllegalArgumentException err) {
                System.out.println("Exit:  \"" + s + "\"");
                System.out.println("EXPECTED ERROR: " + err.getMessage());
                System.out.println("---");
            }
        }
    }
}
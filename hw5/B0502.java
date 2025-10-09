public class B0502 {

    public static boolean checkA(String s) { 
        if (s == null || s.isEmpty()) {  // || - або
            return false;
        }
        char f = s.charAt(0);
        if (!Character.isDigit(f) || f == '0') {
            return false;
        }
        int lenL = Character.getNumericValue(f);
        if (s.length() != 1 + lenL) {
            return false;
        }
        for (int i = 1; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i))) {
                return false;  // знайшли не літеру
            }
        }
        return true;
    }

    public static boolean checkB(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        int dC = 0;
        int dV = -1;  // значення знайденої цифри

        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                dC++;
                dV = Character.getNumericValue(c);
                if (dC > 1) {
                    return false;
                }
            }
        }
        if (dC != 1) {
            return false;
        }
        return dV == s.length();
    }

    public static boolean checkC(String s) { 
        if (s == null || s.isEmpty()) {
            return false;
        }
        int sumD = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                sumD += Character.getNumericValue(c);
            }
        }
        return sumD == s.length();
    }

    public static void main(String[] args) {
        String[] t = {
            "3abc",
            "4abcdX",
            "1a",
            "0abc",
            "a5bc",
            "ab2c3",
            "11111",
            "abc",
            "a2b",
            "5aaaaa",
            "5a5",
            "a1b2c2",
            "a1b2c3",
            "6abcde",
            "9abcdefgh"
        };

        System.out.println("---");
        System.out.println("| line          | property A | property B | property C |");
        System.out.println("---");

        for (String s : t) {
            boolean a = checkA(s);
            boolean b = checkB(s);
            boolean c = checkC(s);

            System.out.printf("| %-13s | %-10s | %-10s | %-10s |\n",
                    s,
                    a ? "TRUE" : "FALSE",
                    b ? "TRUE" : "FALSE",
                    c ? "TRUE" : "FALSE");
        }
        System.out.println("---");
    }
}
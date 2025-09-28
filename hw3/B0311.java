import java.util.Arrays;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

// Abiturient
public class B0311 {
    private String fullName;  
    private String address;  
    private String phone;    
    private int[] grades;    

    public B0311(String fullName, String address, String phone, int[] grades) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
        this.grades = grades;
    }

    public String getFullName() {
        return fullName;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
    public int[] getGrades() {
        return grades;
    }
    public int calculateTotalScore() {
        int sum = 0;
        for (int grade : grades) {
            sum += grade;
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Applicant [Full Name: " + fullName +
               ", Total points: " + calculateTotalScore() +
               ", Assessments: " + Arrays.toString(grades) +
               ", Address: " + address +
               ", Phone: " + phone + "]";
    }

    public static B0311[] getAbiturientsWithScoreLessThan(B0311[] allAbiturients, int requiredMinScore) {
        List<B0311> resultList = new ArrayList<>();
       
        for (B0311 abiturient : allAbiturients) {
            int totalScore = abiturient.calculateTotalScore();
            if (totalScore < requiredMinScore) {
                resultList.add(abiturient);
            }
        }
        return resultList.toArray(new B0311[0]);
    }

    public static B0311[] getTopNAbiturientsByScore(B0311[] allAbiturients, int n) {
        if (n <= 0 || n > allAbiturients.length) {
            if (n >= allAbiturients.length) {  // n недійсне, повертаємо або весь масив, або порожній масив
                 n = allAbiturients.length;
            } else if (n <= 0) {
                 return new B0311[0];
            }
        }

        B0311[] sortedAbiturients = Arrays.copyOf(allAbiturients, allAbiturients.length);
        Arrays.sort(sortedAbiturients, Comparator.comparingInt(B0311::calculateTotalScore).reversed());
        return Arrays.copyOf(sortedAbiturients, n);
    }

    public static void main(String[] args) {
        System.out.println("--- Demonstration of the work of the B0311 class ---");

        B0311[] abiturients = {
            new B0311("Ivanov A.S.", "Kyiv, Shevchenko St., 10", "099-117-27-33", new int[]{85, 90, 78}),  
            new B0311("Petrenko V.M.", "Lviv, Svobody Ave., 5", "067-444-55-67", new int[]{95, 100, 92}),
            new B0311("Sydorova O.I.", "Odesa, Deribasivska St., 3", "063-774-88-92", new int[]{70, 75, 68}),
            new B0311("Kovalchuk R.M.", "Kharkiv, Maidan. Konstitution, 1", "050-293-11-71", new int[]{88, 85, 95})
        };

        System.out.println("\nAll applicants:");
        for (B0311 a : abiturients) {
            System.out.println(a);
        }

        int scoreLimit = 260;
        System.out.println("\n--- Applicants whose total score is LESS than " + scoreLimit + " ---");
        B0311[] lessThanLimit = getAbiturientsWithScoreLessThan(abiturients, scoreLimit);
        if (lessThanLimit.length > 0) {
            for (B0311 a : lessThanLimit) {
                System.out.println(a);
            }
        } else {
            System.out.println("There is no less " + scoreLimit + ".");
        }
       
        int topN = 2;
        System.out.println("\n--- TOP-" + topN + " the HIGHEST sum of points ---");
        B0311[] topAbiturients = getTopNAbiturientsByScore(abiturients, topN);
        if (topAbiturients.length > 0) {
            for (B0311 a : topAbiturients) {
                System.out.println(a);
            }
        } else {
            System.out.println("There are no top-" + topN + " applicants");
        }
        topN = 3;
        System.out.println("\n--- TOP-" + topN + " the HIGHEST total points ---");
        topAbiturients = getTopNAbiturientsByScore(abiturients, topN);
        for (B0311 a : topAbiturients) {
            System.out.println(a);
        }
    }
}
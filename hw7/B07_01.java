import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class B07_01 {
    private static final String FILE_F = "F.bin";
    private static final String FILE_G = "G.bin";

    public static void createBinaryFile(String filename, double[] numbers) throws IOException {
        System.out.println("to file: " + filename);
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {  // DataOutputStream записує примітивні типи даних у бінарному форматі
            for (double number : numbers) {
                dos.writeDouble(number); 
            }
            System.out.println("successfully " + numbers.length + " numbers");
        }
    }

    public static List<Double> readDoubleArrayFromFile(String filename) throws IOException {
        List<Double> numbers = new ArrayList<>();
        System.out.println("from file: " + filename);
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {
            while (true) {
                try {
                    numbers.add(dis.readDouble()); 
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("read " + numbers.size() + " numbers");
            return numbers;
        }
    }

    public static void createFilteredFile(String fileF, String fileG, double a) throws IOException {
        System.out.println("\nfiltering and creating a G file, a = " + a);
        int count = 0;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileF));
             DataOutputStream dos = new DataOutputStream(new FileOutputStream(fileG))) {

            while (true) {
                try {
                    double number = dis.readDouble(); 
                    if (number > a) {
                        dos.writeDouble(number); 
                        count++;
                    }
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("successfully " + count + " in file " + fileG);
        }
    }

    public static void main(String[] args) {
        double[] initialNumbers = {10.5, -3.2, 45.7, 0.1, 12.3, 5.2, 4.97, 100.2};
        double thresholdA = 12.0; 

        System.out.println("--- [B]07.01 ---");
        System.out.print("start: ");
        for (double d : initialNumbers) {
            System.out.print(d + " ");
        }
        
        System.out.println("\n");

        try {
            createBinaryFile(FILE_F, initialNumbers);

            List<Double> readNumbers = readDoubleArrayFromFile(FILE_F);  // зчитує масив (демонстрація)
            System.out.println("read array: " + readNumbers);

            createFilteredFile(FILE_F, FILE_G, thresholdA);

            List<Double> filteredNumbers = readDoubleArrayFromFile(FILE_G);
            System.out.println("G (numbers > " + thresholdA + "): " + filteredNumbers);
            
        } catch (IOException e) {
            System.err.println("\nI/O error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
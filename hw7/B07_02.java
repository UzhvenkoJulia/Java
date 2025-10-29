import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Toy implements Serializable {
    private String name;    
    private double price;   
    private int minAge;     
    private int maxAge;     

    public Toy(String name, double price, int minAge, int maxAge) {
        this.name = name;
        this.price = price;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }

    @Override
    public String toString() {
        return String.format("%s (%.2f UAH, %d-%d years)", name, price, minAge, maxAge);
    }
}

public class B07_02 {
    private static final String FILE_IN = "toys_in.bin";
    private static final String FILE_OUT = "toys_out.bin";

    public static void writeToys(String fName, List<Toy> toys) {
        System.out.println("writing to: " + fName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fName))) {
            for (Toy toy : toys) {
                oos.writeObject(toy);
            }
            System.out.println("successfully wrote " + toys.size() + " toys");
        } catch (IOException e) {
            System.err.println("I/O error during write: " + e.getMessage());
        }
    }

    public static List<Toy> readToys(String fName) {
        List<Toy> result = new ArrayList<>();
        System.out.println("reading from: " + fName);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fName))) {
            while (true) {
                try {
                    Toy toy = (Toy) ois.readObject();
                    result.add(toy);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    System.err.println("class not found during read: " + e.getMessage());
                    break;
                }
            }
            System.out.println("read " + result.size() + " toys");
        } catch (IOException e) {
            if (!e.getMessage().contains("java.io.EOFException")) {
                System.err.println("I/O error during read: " + e.getMessage());
            }
        }
        return result;
    }

    public static void filterAndSave(String fIn, String fOut, int childAge) {
        System.out.println("\n--- filtering Toys for age: " + childAge + " ---");

        List<Toy> allToys = readToys(fIn);
        List<Toy> suitableToys = new ArrayList<>();
        for (Toy toy : allToys) {
            if (childAge >= toy.getMinAge() && childAge <= toy.getMaxAge()) {
                suitableToys.add(toy);
            }
        }

        writeToys(fOut, suitableToys);
        System.out.println("--- found " + suitableToys.size() + " suitable toys ---");
    }

    public static void main(String[] args) {
        List<Toy> initialToys = List.of(
            new Toy("ball", 50.00, 3, 10),
            new Toy("constructor", 450.50, 6, 14),
            new Toy("Barbie doll", 300.99, 4, 8),
            new Toy("radio-controlled car", 620.00, 8, 99),
            new Toy("cubes", 85.00, 1, 3)
        );

        int targetAge = 7; 

        writeToys(FILE_IN, initialToys);
        filterAndSave(FILE_IN, FILE_OUT, targetAge);

        System.out.println("\n*** final check: " + FILE_OUT + " ***");
        List<Toy> finalToys = readToys(FILE_OUT);
        System.out.println("list of toys for " + targetAge + " year old:");
        for (Toy toy : finalToys) {
            System.out.println(" - " + toy);
        }
    }
}
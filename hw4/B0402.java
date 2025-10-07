import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;  // надає методи для реалізації операцій зведення (terminal operations) зі стрімів (Streams)
                                     // у коді він використовується для збирання елементів стріму назад у список (.collect(Collectors.toList())) після сортування або фільтрації

abstract class ElectricD { 
    private String name;
    private int powerW; 
    private double sizeM3; // м^3
    private boolean isPluggedIn;

    public ElectricD(String name, int powerW, double sizeM3) { 
        this.name = name;
        this.powerW = powerW; 
        this.sizeM3 = sizeM3;
        this.isPluggedIn = false;
    }
    
    public abstract double getPrice(); 
    
    public void plugIn() {
        this.isPluggedIn = true;
        System.out.println(name + " included");
    }
    public void plugOut() {
        this.isPluggedIn = false;
        System.out.println(name + " turned off");
    }
    public String getName() {
        return name;
    }
    public int getPowerW() { 
        return powerW; 
    }
    public double getSize() { 
        return sizeM3;
    }
    public boolean isPlugged() { 
        return isPluggedIn;
    }

    @Override
    public String toString() {
        return String.format("%s [power: %dW, size: %.2f m³, enabled: %s, price: %.2f UAH]",
                name, powerW, sizeM3, isPluggedIn ? "yes" : "no", getPrice()); 
    }
}

class LargeA extends ElectricD { 
    private static final double PRICE_MULT = 100000; 

    public LargeA(String name, int powerW, double sizeM3) { 
        super(name, powerW, sizeM3);
    }

    @Override
    public double getPrice() { 
        return (getSize() * PRICE_MULT) + 500; 
    }
}

class SmallA extends ElectricD { 
    private static final double PRICE_MULT = 3000; 

    public SmallA(String name, int powerW, double sizeM3) { 
        super(name, powerW, sizeM3);
    }

    @Override
    public double getPrice() { 
        return (getSize() * PRICE_MULT) + (getPowerW() * 2); 
    }
}

class EnterD extends ElectricD {
    private static final double PRICE_MULT = 7000; 

    public EnterD(String name, int powerW, double sizeM3) { 
        super(name, powerW, sizeM3);
    }

    @Override
    public double getPrice() { 
        return (getSize() * PRICE_MULT) + 1500; 
    }
}

public class B0402 {

    public static void main(String[] args) {

        List<ElectricD> apartmentDevices = new ArrayList<>(); 
        apartmentDevices.add(new LargeA("refrigerator", 150, 1.2));
        apartmentDevices.add(new LargeA("washing machine", 2500, 0.5)); 
        apartmentDevices.add(new SmallA("toaster", 400, 0.005)); 
        apartmentDevices.add(new SmallA("hair dryer", 1200, 0.003)); 
        apartmentDevices.add(new EnterD("TV (hall)", 90, 0.15)); 
        apartmentDevices.add(new EnterD("TV (kitchen)", 70, 0.05)); 
        apartmentDevices.add(new LargeA("dishwasher", 1500, 0.4)); 

        System.out.println("🏠 a list of all appliances in the apartment");
        apartmentDevices.forEach(System.out::println);
        System.out.println("---\n");

        apartmentDevices.get(0).plugIn();
        apartmentDevices.get(2).plugIn();
        apartmentDevices.get(4).plugIn();
        System.out.println("---\n");

        int totalPower = getTotalPower(apartmentDevices); 
        System.out.printf("⚡total power consumption of switched on devices: %d W\n", totalPower);
        System.out.println("---\n");

        List<ElectricD> sortedDevices = sortBySize(apartmentDevices); 
        System.out.println("📏 devices sorted by size:");
        sortedDevices.forEach(System.out::println);
        System.out.println("---\n");

        double minPrice = 3000;
        double maxPrice = 100000;

        List<ElectricD> filteredDevices = filterByPrice(apartmentDevices, minPrice, maxPrice); 

        System.out.printf("💰 devices in the price range from %.2f UAH to %.2f UAH:\n", minPrice, maxPrice);
        if (filteredDevices.isEmpty()) {
            System.out.println("  — no devices found");
        } else {
            filteredDevices.forEach(System.out::println);
        }
        System.out.println("---\n");
    }

    public static int getTotalPower(List<ElectricD> devices) { 
        return devices.stream()
                .filter(ElectricD::isPlugged)  
                .mapToInt(ElectricD::getPowerW)  
                .sum();
    }
    public static List<ElectricD> sortBySize(List<ElectricD> devices) { 
        return devices.stream()
                .sorted(Comparator.comparingDouble(ElectricD::getSize).reversed()) 
                .collect(Collectors.toList());
    }
    public static List<ElectricD> filterByPrice(List<ElectricD> devices, double minPrice, double maxPrice) { 
        return devices.stream()
                .filter(d -> {
                    double price = d.getPrice();
                    return price >= minPrice && price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
}
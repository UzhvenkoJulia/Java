import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. Student: Клас, що моделює стан студента (гроші, кредити, спеціалізація)
 * 2. Element/Visitor: Інтерфейси для шаблону
 * 3. ConcreteElements: Класи для кроків діяльності (TeachDisciplineStep, PayHostelStep...)
 * 4. ConcreteVisitor: StudentLifeSimulator, що містить логіку впливу кроків на Student
 */


// 1. МОДЕЛЮВАННЯ ДАНИХ


class Student {
    public enum Specialization {
        HUMANITARIAN,
        NATURAL,
        HUMANITARIAN_NATURAL
    }

    private final Specialization specialization;
    private final int requiredCredits;
    private int currentCredits;
    private int currentMoney;
    private boolean hasDiploma;
    private boolean isExpelled;
    private String expulsionReason;

    public Student(String spec, int requiredCredits, int initialMoney) {
        // "humanitarian-natural" -> HUMANITARIAN_NATURAL
        this.specialization = Specialization.valueOf(spec.toUpperCase().replace('-', '_'));
        this.requiredCredits = requiredCredits;
        this.currentMoney = initialMoney;
        this.currentCredits = 0;
        this.hasDiploma = false;
        this.isExpelled = false;
        this.expulsionReason = "";
    }

    // + кредити  
    public void addCredits(int credits) {
        if (!isExpelled && !hasDiploma) {
            this.currentCredits += credits;
            if (this.currentCredits >= this.requiredCredits) {
                this.hasDiploma = true;
            }
        }
    }

    // Зміна грошового балансу
    public void changeMoney(int amount) {
        if (!isExpelled && !hasDiploma) {
            this.currentMoney += amount;
            // Перевірка на відрахування через брак грошей
            if (this.currentMoney < 0) {
                this.isExpelled = true;
                this.expulsionReason = "Відраховано через брак коштів для існування";
            }
        }
    }
    
    public Specialization getSpecialization() { return specialization; }
    public int getRequiredCredits() { return requiredCredits; }
    public int getCurrentCredits() { return currentCredits; }
    public int getCurrentMoney() { return currentMoney; }
    public boolean hasDiploma() { return hasDiploma; }
    public boolean isExpelled() { return isExpelled; }
    public String getExpulsionReason() { return expulsionReason; }
    
    public String getFinalResult() {
        if (hasDiploma) {
            return "Отримав диплом. Фінальний баланс: " + currentMoney + " грн. Набрано кредитів: " + currentCredits;
        } else if (isExpelled) {
            return "Відраховано. Причина: " + expulsionReason + " Фінальний баланс: " + currentMoney + " грн. Набрано кредитів: " + currentCredits;
        } else {
            return "Навчання не завершено (недостатньо кредитів). Фінальний баланс: " + currentMoney + " грн. Набрано кредитів: " + currentCredits;
        }
    }
}


// 2. ІНТЕРФЕЙСИ ШАБЛОНУ ВІДВІДУВАЧ
// Element.java: Інтерфейс для кроків діяльності студента
// Visitor.java: Інтерфейс для симулятора життєдіяльності

interface Element {
    void accept(Visitor visitor);
}

interface Visitor {
    void visit(TeachDisciplineStep step);
    void visit(PayHostelStep step);
    void visit(PayFoodStep step);
    void visit(ObtainScholarshipStep step);
    void visit(ObtainMoneyFromParentsStep step);
}


// 3. КОНКРЕТНІ ЕЛЕМЕНТИ (CONCRETE ELEMENTS) - КРОКИ ДІЯЛЬНОСТІ


class TeachDisciplineStep implements Element {
    private final Student.Specialization disciplineType;
    private final int credits;

    public TeachDisciplineStep(String type, int credits) {
        this.disciplineType = Student.Specialization.valueOf(type.toUpperCase());
        this.credits = credits;
    }

    public Student.Specialization getDisciplineType() { return disciplineType; }
    public int getCredits() { return credits; }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class PayHostelStep implements Element {
    private final int cost;
    public PayHostelStep(int cost) { this.cost = cost; }
    public int getCost() { return cost; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class PayFoodStep implements Element {
    private final int cost;
    public PayFoodStep(int cost) { this.cost = cost; }
    public int getCost() { return cost; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class ObtainScholarshipStep implements Element {
    private final int amount;
    public ObtainScholarshipStep(int amount) { this.amount = amount; }
    public int getAmount() { return amount; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

class ObtainMoneyFromParentsStep implements Element {
    private final int amount;
    public ObtainMoneyFromParentsStep(int amount) { this.amount = amount; }
    public int getAmount() { return amount; }
    
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}


// 4. КОНКРЕТНИЙ ВІДВІДУВАЧ - СИМУЛЯТОР

class StudentLifeSimulator implements Visitor {
    private final Student student;

    public StudentLifeSimulator(Student student) {
        this.student = student;
    }

    // Перевірка відповідності профілю викладача та студента
    private boolean canTeacherTeach(Student.Specialization studentSpec, Student.Specialization disciplineType) {
        if (studentSpec == Student.Specialization.HUMANITARIAN_NATURAL) {
            return true; // ГПН можуть навчатися в обох
        }
        return disciplineType == studentSpec;
    }

    @Override
    public void visit(TeachDisciplineStep step) {
        if (student.hasDiploma() || student.isExpelled()) return;

        if (canTeacherTeach(student.getSpecialization(), step.getDisciplineType())) {
            student.addCredits(step.getCredits());
        }
    }

    @Override
    public void visit(PayHostelStep step) {
        if (student.hasDiploma() || student.isExpelled()) return;
        student.changeMoney(-step.getCost());
    }

    @Override
    public void visit(PayFoodStep step) {
        if (student.hasDiploma() || student.isExpelled()) return;
        student.changeMoney(-step.getCost());
    }

    @Override
    public void visit(ObtainScholarshipStep step) {
        if (student.hasDiploma() || student.isExpelled()) return;
        student.changeMoney(step.getAmount());
    }

    @Override
    public void visit(ObtainMoneyFromParentsStep step) {
        if (student.hasDiploma() || student.isExpelled()) return;
        student.changeMoney(step.getAmount());
    }
}


public class B12_01 {
public static Element parseStep(String line) {
    String trimmedLine = line.trim();
    // .split("\\s+") для коректної обробки множинних пробілів
    String[] parts = trimmedLine.split("\\s+");
    if (parts.length < 3) return null; 
    
    String command = parts[0];
    
    try {
        switch (command.toLowerCase()) {
            case "teach":
                // teach humanitarian/natural [credits]
                String disciplineType = parts[1];
                int credits = Integer.parseInt(parts[2]);
                if (disciplineType.equalsIgnoreCase("humanitarian") || disciplineType.equalsIgnoreCase("natural")) {
                    return new TeachDisciplineStep(disciplineType, credits);
                }
                break;
            case "pay":
                // pay hostel [cost] / pay canteen [cost] / pay food [cost]
                String item = parts[1];
                int cost = Integer.parseInt(parts[2]);
                if (item.equalsIgnoreCase("hostel")) {
                    return new PayHostelStep(cost);
                } 
                // обробка 'pay canteen' як 'PayFoodStep'
                else if (item.equalsIgnoreCase("food") || item.equalsIgnoreCase("canteen")) { 
                    return new PayFoodStep(cost);
                }
                break;
            case "obtain":
                // obtain scholarship [amount] / obtain money parents [amount] / obtain help [amount]
                String source = parts[1];
                int amount = Integer.parseInt(parts[2]);
                if (source.equalsIgnoreCase("scholarship")) {
                    return new ObtainScholarshipStep(amount);
                } 
                // обробка 'obtain help' та 'obtain money' як 'ObtainMoneyFromParentsStep'
                else if (source.equalsIgnoreCase("parents") || source.equalsIgnoreCase("help") || (source.equalsIgnoreCase("money") && parts[2].equalsIgnoreCase("parents"))) { 
                    // Якщо формат 'obtain money parents 2000'
                    if (source.equalsIgnoreCase("money") && parts.length > 3 && parts[2].equalsIgnoreCase("parents")) {
                        amount = Integer.parseInt(parts[3]);
                    }
                    return new ObtainMoneyFromParentsStep(amount);
                }
                break;
        }
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
        // System.err.println("Помилка: Невірний формат у рядку: " + trimmedLine); 
    }
    return null;
}

    public static void runSimulation(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка: Не вдалося прочитати файл " + filename + ". Переконайтеся, що він існує");
            return;
        }

        if (lines.size() < 3) {
            System.out.println("Файл " + filename + " містить недостатньо початкових даних");
            return;
        }

        try {
            String specStr = lines.get(0).trim();
            int requiredCredits = Integer.parseInt(lines.get(1).trim());
            int initialMoney = Integer.parseInt(lines.get(2).trim());

            Student student = new Student(specStr, requiredCredits, initialMoney);
            
            Visitor simulator = new StudentLifeSimulator(student);

            List<Element> steps = new ArrayList<>();
            for (int i = 3; i < lines.size(); i++) {
                Element step = parseStep(lines.get(i));
                if (step != null) {
                    steps.add(step);
                }
            }

            System.out.println("\n===");
            System.out.println(">>> симуляція: " + filename);
            System.out.println("Спеціальність: " + student.getSpecialization());
            System.out.println("Потрібно кредитів: " + student.getRequiredCredits() + "; Початковий баланс: " + initialMoney + " грн");
            System.out.println("---");

            for (Element step : steps) {
                // Зупинка симуляції, якщо студент досяг кінцевого стану
                if (student.hasDiploma() || student.isExpelled()) {
                    break;
                }
                step.accept(simulator);
            }

            System.out.println("\n--- результат ---");
            System.out.println("Чи отримав диплом: " + (student.hasDiploma() ? "Так" : "Ні"));
            System.out.println(student.getFinalResult());
            System.out.println("===");

        } catch (NumberFormatException e) {
             System.err.println("Помилка: Невірний числовий формат у початкових даних файлу " + filename);
        } catch (IllegalArgumentException e) {
             System.err.println("Помилка: Невірна спеціальність у файлі " + filename);
        }
    }

    public static void main(String[] args) {
        String[] inputFiles = {"input03.txt", "input07.txt"}; 
        
        for (String filename : inputFiles) {
            runSimulation(filename);
        }
    }
}
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class B10_06_Client {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {
            System.out.println("Connected to the task server B10.06!");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner in = new Scanner(socket.getInputStream());
            Scanner console = new Scanner(System.in);

            System.out.print("natural number (divisor): ");
            while (!console.hasNextInt()) { 
                System.out.println("enter a whole number!");
                console.next();
            }
            int divisor = console.nextInt();
            console.nextLine(); 
            
            out.println(divisor);

            System.out.println("string of numbers separated by a space (or 'exit' to exit):");

            while (true) {
                System.out.print(">");
                String line = console.nextLine();

                out.println(line);

                if (line.equalsIgnoreCase("exit")) {
                    break;
                }

                if (in.hasNextInt()) {
                    int result = in.nextInt();
                    in.nextLine(); 
                    System.out.println("Number of numbers that are multiples " + divisor + ": " + result);
                }
            }

        } catch (IOException e) {
            System.out.println("Could not connect to server");
        }
    }
}
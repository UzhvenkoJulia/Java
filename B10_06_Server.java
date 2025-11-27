import java.io.*;
import java.net.*;
import java.util.Scanner;

public class B10_06_Server {
    public static void main(String[] args) {
        int port = 5000; 

        System.out.println("Server [B10_06] is starting...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for the client at the port " + port + "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("The client is connected!");

            Scanner in = new Scanner(clientSocket.getInputStream());
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            int divisor = 1; // уникнення ділення на 0, шук дільник
            if (in.hasNextInt()) {
                divisor = in.nextInt();
                in.nextLine();
                System.out.println("Divisor obtained K = " + divisor);
            }

            while (in.hasNextLine()) {
                String line = in.nextLine();
                
                if (line.equalsIgnoreCase("exit")) {
                    break;
                }

                System.out.println("Received a string for processing: " + line);

                int count = 0;
                String[] parts = line.split("\\s+"); // рядок на окремі частини

                for (String part : parts) {
                    try {
                        int num = Integer.parseInt(part);
                        if (num % divisor == 0) {
                            count++;
                        }
                    } catch (NumberFormatException e) {
                    
                    }
                }

                out.println(count);
            }
            
            System.out.println("The work is completed");

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}
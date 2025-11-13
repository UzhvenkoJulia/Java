import java.io.*;
import java.util.concurrent.*;

public class B09_01 {
    private static final String EOF_MARKER = "---EOF---";
    private static final int QUEUE_CAPACITY = 10;
    private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    public static void main(String[] args) {
        final long T1 = 500; // мілісекудни
        final long T2 = 700; 
        final long T3 = 1000; 

        final String INPUT_FILE = "F_input.txt";
        final String OUTPUT_FILE_1 = "Output_1.txt";
        final String OUTPUT_FILE_2 = "Output_2.txt";

        createDummyFile(INPUT_FILE);

        Thread readerThread = new Thread(new Reader(INPUT_FILE, T1, queue));
        readerThread.setName("Reader-T1");
        readerThread.start();

        Thread processor1 = new Thread(new Processor(OUTPUT_FILE_1, T2, queue, 1));
        processor1.setName("Processor-1-T2");
        processor1.start();

        Thread processor2 = new Thread(new Processor(OUTPUT_FILE_2, T3, queue, 2));
        processor2.setName("Processor-2-T3");
        processor2.start();

        try {
            readerThread.join();
            processor1.join();
            processor2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ synchronization [B]09.01 completed, files: "
                + OUTPUT_FILE_1 + " and " + OUTPUT_FILE_2);
    }

    static class Reader implements Runnable {
        private final String filename;
        private final long readTime;
        private final BlockingQueue<String> queue;

        public Reader(String filename, long readTime, BlockingQueue<String> queue) {
            this.filename = filename;
            this.readTime = readTime;
            this.queue = queue;
        }

        @Override
        public void run() {
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                System.out.println("start: " + Thread.currentThread().getName());
                while ((line = br.readLine()) != null) {
                    Thread.sleep(readTime); 
                    queue.put(line);
                    System.out.println(Thread.currentThread().getName() + " read: " + line);
                }
                
                queue.put(EOF_MARKER);
                queue.put(EOF_MARKER); 

            } catch (IOException | InterruptedException e) {
                System.err.println("error in Reader: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            System.out.println("end: " + Thread.currentThread().getName());
        }
    }

    static class Processor implements Runnable {
        private final String outputFilename;
        private final long processingTime;
        private final BlockingQueue<String> queue;
        private final int id;

        public Processor(String outputFilename, long processingTime, 
                         BlockingQueue<String> queue, int id) {
            this.outputFilename = outputFilename;
            this.processingTime = processingTime;
            this.queue = queue;
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("start: " + Thread.currentThread().getName());
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename, false))) {
                while (true) {
                    String line = queue.take(); 

                    if (line.equals(EOF_MARKER)) {
                        queue.put(EOF_MARKER); 
                        break; 
                    }

                    Thread.sleep(processingTime); 
                    bw.write(line);
                    bw.newLine();
                    System.out.println(Thread.currentThread().getName() + 
                                       " processed: " + line + 
                                       " -> " + outputFilename);
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("error in Processor " + id + ": " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            System.out.println("end: " + Thread.currentThread().getName());
        }
    }
    
    private static void createDummyFile(String filename) {
        try (PrintWriter pw = new PrintWriter(filename)) {
            for (int i = 1; i <= 15; i++) {
                pw.println("line " + i + "from File F");
            }
            System.out.println("input file created: " + filename);
        } catch (FileNotFoundException e) {
            System.err.println("failed to create file: " + e.getMessage());
        }
    }
}
import java.util.concurrent.*;
import java.util.Random;

public class B09_07 {
    
    private static final int N_DOCTORS = 3; 
    private static final int N_PATIENTS = 15; 
    
    private static final int T1 = 500; 
    private static final int T2 = 2000; 
    private static final int T3 = 1000; 
    private static final int T4 = 4000; 
    
    private static Semaphore doctorsSemaphore = new Semaphore(N_DOCTORS);
    private static Random random = new Random();
    
    private static final long[] doctorWaitTimes = new long[N_DOCTORS];

    public static void main(String[] args) {
        System.out.println("🏥 hospital work simulation with" + 
                           N_DOCTORS + "doctors");
        
        for (int i = 0; i < N_DOCTORS; i++) {
            doctorWaitTimes[i] = random.nextInt(T4 - T3 + 1) + T3; 
            System.out.println("doctor " + (i + 1) + 
                               " has a fixed reception time: " + 
                               doctorWaitTimes[i] + " milliseconds");
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(N_PATIENTS);
        
        for (int i = 1; i <= N_PATIENTS; i++) {
            try {
                long arrivalDelay = random.nextInt(T2 - T1 + 1) + T1;
                Thread.sleep(arrivalDelay); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            executor.submit(new Patient(i));
        }
        
        executor.shutdown(); 
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n✅ simulation [B]09.07 completed");
    }
    
    static class Patient implements Runnable {
        private final int patientId;

        public Patient(int patientId) {
            this.patientId = patientId;
        }

        @Override
        public void run() {
            String logPrefix = "patient " + patientId;
            System.out.println(logPrefix + " arrived at the hospital");

            try {
                System.out.println(logPrefix + " looking for a doctor. Available: " + doctorsSemaphore.availablePermits());
                doctorsSemaphore.acquire(); 
                
                int doctorIndex = findDoctorIndex();
                long consultationTime = doctorWaitTimes[doctorIndex];
                
                System.out.println(">>> " + logPrefix + " starts an appointment with the Doctor " + 
                                   (doctorIndex + 1) + ". Time: " + consultationTime + " milliseconds");
                
                Thread.sleep(consultationTime);
                
                System.out.println("<<< " + logPrefix + " completed the appointment with the Doctor " + 
                                   (doctorIndex + 1) + " and left the hospital");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doctorsSemaphore.release(); 
                System.out.println(logPrefix + " released the doctor. Free: " + doctorsSemaphore.availablePermits());
            }
        }
        
        // без індексу, лікар, час якого зафіксовано, відповідає вільному дозволу
        // у реальному житті потрібен був би додатковий механізм відстеження стану лікарів
        private int findDoctorIndex() {
            // вільний лікар має найнижчий індекс серед тих, хто ще не зайнятий
            return (N_DOCTORS - doctorsSemaphore.availablePermits() - 1);
        }
    }
}